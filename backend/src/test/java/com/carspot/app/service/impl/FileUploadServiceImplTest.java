//package com.carspot.app.service.impl;
//
//import com.carspot.app.BackendApplication;
//import com.carspot.app.BackendApplicationTests;
//import com.carspot.app.entity.Post;
//import com.carspot.app.entity.PostImage;
//import com.carspot.app.entity.User;
//import com.carspot.app.entity.UserAvatar;
//import com.carspot.app.exception.exceptions.FileUploadException;
//import com.carspot.app.repository.PostImageRepository;
//import com.carspot.app.repository.PostRepository;
//import com.carspot.app.repository.UserAvatarRepository;
//import com.carspot.app.repository.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.carspot.app.utils.TestUtils.convertMultipartFileToFile;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class FileUploadServiceImplTest {
//
//    @InjectMocks
//    private FileUploadServiceImpl fileUploadService;
//
//    @Mock
//    private PostImageRepository postImageRepository;
//
//    @Mock
//    private UserAvatarRepository userAvatarRepository;
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AmazonS3 amazonS3Client;
//
//    private Post postOne;
//
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//
//        user = new User(1, "admin", "admin@gmail.com", "+27339923823", "password");
//
//        fileUploadService = new FileUploadServiceImpl(userAvatarRepository, userRepository, postImageRepository, postRepository, null);
//
//        postOne = new Post(1L,
//                "This is my first post on the website",
//                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
//                "Toyota", "Supra", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
//                2019, "Province 1", "city 1");
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//
//    @Test
//    @Disabled
//    void canUploadPostImages() {
//
//        MockMultipartFile multipartFile1 = new MockMultipartFile("image", "test.jpg",
//                "image/jpg", "Some bytes".getBytes());
//        MockMultipartFile multipartFile2= new MockMultipartFile("image", "test.jpg",
//                "image/jpg", "Some bytes".getBytes());
//
//        List<MultipartFile> files = List.of(multipartFile1, multipartFile2);
//
//        when(postRepository.findPostById(anyLong())).thenReturn(postOne);
//
//        PostImage postImage1 = new PostImage("33344", "ee", postOne);
//        PostImage postImage2 = new PostImage("333344", "33", postOne);
//
//
//
//        //List<PostImage> postImageList = new ArrayList<>(List.of(postImage1, postImage2));
//
//        fileUploadService.uploadPostImages(files, postOne.getId(), "admin@gmail.com");
//
//        assertThat(postOne.getPostImages().size()).isGreaterThan(0);
//    }
//
//    @Test
//    @Disabled
//    void willThrowIfFileExtensionIsInvalidWhenUploadingPostImages() {
//
//    }
//
////    @Test
////    void willThrowIfUploadPostImagesMoreThanSeven() {
//////        List<ImageUpload> imageUploadList = new ArrayList<>();
////        // Uploading 8 images, will throw error because max is 7 images
////        List<MultipartFile> files = new ArrayList<>();
////        for (int i = 0; i < 8; i++) {
////            files.add(new MockMultipartFile("image", "test.jpg", "image/jpg", "Some bytes".getBytes()));
////        }
////
////        when(postRepository.findPostById(anyLong())).thenReturn(postOne);
////        assertThrows(FileUploadException.class, () -> {
////            fileUploadService.uploadPostImages(files, postOne.getId(), "admin@gmail.com");
////        }, "Maximum of 7 images can be added per post");
////
////    }
//
//    @Test
//    @Disabled
//    void canUploadUserAvatar() {
//        MockMultipartFile userAvatarMultipart = new MockMultipartFile("image", "test.jpg", "image/jpg",
//                "Some bytes".getBytes());
//
//        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);
//
//        UserAvatar avatar = new UserAvatar("33344", "https://mybucket.s3.amazonaws.com/test.jpg", user);
//
//        fileUploadService.uploadUserAvatar(userAvatarMultipart, "admin@gmail.com");
//        user.setAvatar(avatar);
//
//        assertEquals(user.getAvatar().getId(), 1);
//        verify(amazonS3Client).putObject(any());
//        verify(userAvatarRepository).save(any());
//    }
//
//    @Test
//    @Disabled
//    void willThrowIfFileExtensionIsInvalidWhenUploadingUserAvatar() {
//
//    }
//
//    @Test
//    @Disabled
//    void canUpdateUserAvatar()  {
//        MockMultipartFile userAvatarMultipart1 = new MockMultipartFile("image", "test.jpg", "image/jpg", "Some bytes".getBytes());
//        MockMultipartFile userAvatarMultipart2 = new MockMultipartFile("image", "updated.jpg", "image/jpg", "Some bytes".getBytes());
//
//
//        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);
//
//        UserAvatar avatar = new UserAvatar("33344", "33", user);
//
//        fileUploadService.uploadUserAvatar(userAvatarMultipart1, "admin@gmail.com");
//        user.setAvatar(avatar);
//
//
//        UserAvatar updatedAvatar = new UserAvatar("33344", "44", user);
//
//
//
//        fileUploadService.uploadUserAvatar(userAvatarMultipart2, "admin@gmail.com");
//
//        user.setAvatar(updatedAvatar);
//
//
//        verify(userAvatarRepository, times(2)).save(any());
//    }
//
//
//    @Test
//    void canDeletePostImageById() {
//        //MockMultipartFile postImageMultipart = new MockMultipartFile("image", "test.jpg", "image/jpg", "Some bytes".getBytes());
//        PostImage postImage = new PostImage("33344", "ee", postOne);
//
//        when(postImageRepository.findPostImageById(anyString())).thenReturn(postImage);
//
//
//        doNothing().when(postImageRepository).delete(any(PostImage.class));
//        // Act & Assert
//        fileUploadService.deletePostImageById(postImage.getId());
//
//
//        verify(amazonS3Client).deleteObject(any());
//        verify(postImageRepository).delete(any(PostImage.class));
//    }
//
//    @Test
//    void canDeleteAllPostImagesOfAPost() {
//        PostImage postImage1 = new PostImage("33344", "ee", postOne);
//        PostImage postImage2 = new PostImage("33344", "ee", postOne);
//
//        List<PostImage> postImageList = List.of(postImage1, postImage2);
//
//        fileUploadService.deletePostImages(postImageList);
//
//        verify(amazonS3Client, times(2)).deleteObject(any());
//        verify(postImageRepository, times(2)).delete(any(PostImage.class));
//
//
//    }
//
//    @Test
//    void canFindPostImageByPostImageId() {
//        PostImage postImage = new PostImage("33344", "ee", postOne);
//
//        when(postImageRepository.findPostImageById(anyString())).thenReturn(postImage);
//
//        PostImage foundPostImage = fileUploadService.findPostImageById(postImage.getId());
//
//        assertEquals("33344", foundPostImage.getId());
//        verify(postImageRepository).findPostImageById(anyString());
//    }
//
//
//
//}