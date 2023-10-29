package com.carspot.app.dto;

import com.carspot.app.entity.UserAvatar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserReviewAndAvatarDTO {

    private Long id;
    private String reviewerFullName;
    private String reviewerEmailAddress;
    private LocalDateTime createdAt;
    private String review;

    private UserAvatar userAvatar;
//    private String fileId;
//    private byte[] data;
}
