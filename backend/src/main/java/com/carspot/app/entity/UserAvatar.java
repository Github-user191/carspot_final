package com.carspot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class UserAvatar {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @NotNull
    private String imageUrl;

    public UserAvatar(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(unique = true, name = "user_id", referencedColumnName = "id")
    private User user;
}
