package com.carspot.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank(message = "Required")
    private String reviewerFullName;

//    @Email(message = "Email is invalid")
//    @NotBlank(message = "Required")
    private String reviewerEmailAddress;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotBlank(message = "Required")
    @Size(min = 20,message = "Minimum of 20 characters")
    private String review;

    public Review(Long id, String reviewerFullName, String reviewerEmailAddress, String review) {
        this.id = id;
        this.reviewerFullName = reviewerFullName;
        this.reviewerEmailAddress = reviewerEmailAddress;
        this.review = review;
        this.createdAt = LocalDateTime.now();
    }

    @OneToOne()
    @JsonIgnore
    // unique = true
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_USER_ID"))
    private User user;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
