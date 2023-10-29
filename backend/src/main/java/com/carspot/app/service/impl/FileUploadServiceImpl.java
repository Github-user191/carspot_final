package com.carspot.app.service.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobItem;
import com.carspot.app.config.AzureConfig;
import com.carspot.app.entity.*;
import com.carspot.app.exception.exceptions.FileUploadException;
import com.carspot.app.repository.*;
import com.carspot.app.service.FileUploadService;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final UserAvatarRepository userAvatarRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;
    private final AzureConfig azureConfig;


    public FileUploadServiceImpl(UserAvatarRepository userAvatarRepository, UserRepository userRepository, PostImageRepository postImageRepository, PostRepository postRepository, AzureConfig azureConfig) {
        this.userAvatarRepository = userAvatarRepository;
        this.userRepository = userRepository;
        this.postImageRepository = postImageRepository;
        this.postRepository = postRepository;
        this.azureConfig = azureConfig;
    }


    @Override
    public List<PostImage> getAllPostImages() {
        BlobContainerClient containerClient = azureConfig.blobClient();

        return containerClient.listBlobs().stream()
            .map(blob -> {
                BlobClient blobClient = containerClient.getBlobClient(blob.getName());
                System.out.println(blobClient.getBlobName());
                System.out.println(blob.getName());
                return new PostImage(blobClient.getBlobUrl());
            }).collect(Collectors.toList());
    }

    @Override
    public Integer findPostImagesCountByPostId(Long postId, String emailAddress) {
        return postImageRepository.countPostImagesByPostId(postId, emailAddress);
    }

    @Override
    public PostImage findPostImageById(String id) {
        PostImage postImage = postImageRepository.findPostImageById(id);

        if (postImage == null) {
            throw new FileUploadException("No image found");
        }

//        if(!postImage.getFileUploader().equals(emailAddress)) {
//            throw new FileUploadException("Post image not found in your account");
//        }
        return postImage;
    }

    @Override
    public void deleteUserAvatar(UserAvatar userAvatar) {
        String fileName = userAvatar.getImageUrl().substring(userAvatar.getImageUrl().lastIndexOf("/") + 1);
        azureConfig.blobClient().getBlobClient(fileName).deleteIfExists();
        userAvatarRepository.delete(userAvatar);
    }




    @Override
    public void deletePostImageById(String id) {
        PostImage postImage = findPostImageById(id);
        String fileName = postImage.getImageUrl().substring(postImage.getImageUrl().lastIndexOf("/") + 1);
        System.out.println("to delete " + fileName);
        azureConfig.blobClient().getBlobClient(fileName).deleteIfExists();
        postImageRepository.delete(postImage);
    }


    @Override
    public void deletePostImages(List<PostImage> postImages) {
        for (PostImage postImage : postImages) {
            String fileName = postImage.getImageUrl().substring(postImage.getImageUrl().lastIndexOf("/") + 1);

            azureConfig.blobClient().getBlobClient(fileName).deleteIfExists();
            postImageRepository.delete(postImage);
        }
    }

    @Override
    public List<PostImage> uploadPostImages(List<MultipartFile> postImages, Long postId, String emailAddress) throws IOException {

        System.out.println("in upload post img");
        List<PostImage> imageList = new ArrayList<>();
        Post post = postRepository.findPostById(postId);

        if (postImages != null) {
            for (MultipartFile file : postImages) {
                BlobClient blobClient = azureConfig.blobClient().getBlobClient(file.getOriginalFilename());
                blobClient.upload(file.getInputStream(), file.getSize(), true);

                PostImage postImage = new PostImage(blobClient.getBlobUrl());
                postImage.setPost(post);
                imageList.add(postImage);
            }
            postImageRepository.saveAll(imageList);
            post.setPostImages(imageList);


        }


        return imageList;
    }

    // Upload image to AWS S3.
    @Override
    public UserAvatar uploadUserAvatar(MultipartFile file, String emailAddress) throws IOException {

        List<String> validExtensions = Arrays.asList("jpeg", "jpg", "png");

        // Get extension of MultipartFile
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        // if the uploaded file doesnt contain any of the extentions in validExtentions
        if (!validExtensions.contains(extension)) {
            throw new FileUploadException("Only .jpg, .jpeg or .png file types are allowed");
        } else {

            User user = userRepository.findUserByEmailAddress(emailAddress);

            BlobClient uploadedBlob = azureConfig.blobClient().getBlobClient(file.getOriginalFilename());
            uploadedBlob.upload(file.getInputStream(), file.getSize(), true);

            // Save image information on MongoDB and return them.
            UserAvatar userAvatar = null;

            if (user.getAvatar() != null) {
                userAvatar = userAvatarRepository.getById(user.getAvatar().getId());

                String fileName = userAvatar.getImageUrl().substring(userAvatar.getImageUrl().lastIndexOf("/") + 1);

                BlobClient existingBlob = azureConfig.blobClient().getBlobClient(fileName);

                existingBlob.deleteIfExists();
                userAvatar.setImageUrl(uploadedBlob.getBlobUrl());

            } else {
                userAvatar = new UserAvatar(uploadedBlob.getBlobUrl());
                userAvatar.setUser(user);
            }

            return userAvatarRepository.save(userAvatar);
        }

    }

}
