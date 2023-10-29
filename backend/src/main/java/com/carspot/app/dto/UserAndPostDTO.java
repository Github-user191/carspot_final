package com.carspot.app.dto;

import com.carspot.app.entity.UserAvatar;
import com.carspot.app.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAndPostDTO {
    private Long id;
    private String fullName;
    private String emailAddress;
    private String mobileNumber;
    private LocalDateTime dateJoined;

//    private Date expiredAt;

    private UserAvatar userAvatar;
    private List<Post> posts;

}
