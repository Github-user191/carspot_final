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
public class PostImage {

        @Id
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        @GeneratedValue(generator = "uuid")
        private String id;

        @NotNull
        private String imageUrl;

        public PostImage(String imageUrl) {
            this.imageUrl = imageUrl;
        }


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id")
        @JsonIgnore
        private Post post;

}
