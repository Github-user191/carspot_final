package com.carspot.app.service;

import com.carspot.app.entity.PostImage;
import com.carspot.app.entity.UserAvatar;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {

    // Related to Post images

    List<PostImage> uploadPostImages(List<MultipartFile> postImages, Long postId, String emailAddress) throws IOException;
    Integer findPostImagesCountByPostId(Long postId, String emailAddress);
    PostImage findPostImageById(String id);

    void deletePostImageById(String id);

    void deleteUserAvatar(UserAvatar userAvatar);
    void deletePostImages(List<PostImage> postImage);
    List<PostImage> getAllPostImages();
    UserAvatar uploadUserAvatar(MultipartFile avatar, String emailAddress) throws IOException;
}
