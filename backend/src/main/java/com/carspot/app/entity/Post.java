package com.carspot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

// PARENT
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Serializable {

    @Transient
    @JsonIgnore
    private long EXPIRATION_TIME_MONTHS = 2;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Required")
    @Size(min=20, message = "Minimum of 20 characters")
    @Size(max=100, message = "Maximum of 100 characters")
    @Column(unique = true)
    private String title;

    @NotBlank(message = "Required")
    @Size(min = 50, message = "Minimum of 50 characters")
    @Size(max = 2000, message = "Maximum of 2000 characters")
    @Column(length = 2000)
    private String description;

    @NotBlank(message = "Required")
    @Column()
    private String brand;

    @NotBlank(message = "Required")
    @Column()
    private String model;

    @NotNull(message = "Required")
    @Column()
    private Double price;

    @NotBlank(message = "Required")
    @Column()
    private String color;

    @NotNull(message = "Required")
    @Column()
    private Double kilometers;

    @NotBlank(message = "Required")
    @Column()
    private String bodyType;

    @NotBlank(message = "Required")
    @Column()
    private String fuelType;

    @NotBlank(message = "Required")
    @Column()
    private String transmission;

    @NotNull(message = "Required")
    @Column()
    private Integer year;

    @NotBlank(message = "Required")
    private String province;


    @NotBlank(message = "Required")
    @Column()
    private String city;

    @Column(name ="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="expired_at", updatable = false)
    private LocalDateTime expiredAt;

    @Column(name="active", nullable = false)
    private boolean active = true;

    @Column(name="post_creator_email")
    private String postCreatorEmail;

    @Column(name="post_creator_name")
    private String postCreatorName;


    public Post(Long id, String title, String description, String brand, String model, Double price, String color, Double kilometers, String bodyType, String fuelType, String transmission, Integer year, String province, String city) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.color = color;
        this.kilometers = kilometers;
        this.bodyType = bodyType;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.year = year;
        this.province = province;
        this.city = city;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = calculateExpiryDate(createdAt);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.expiredAt = calculateExpiryDate(createdAt);

    }

    private LocalDateTime calculateExpiryDate(LocalDateTime createdAt) {
        return createdAt.plusMonths(EXPIRATION_TIME_MONTHS);
    }

    @JsonIgnore
    public boolean isPostExpired() {
        LocalDateTime current = LocalDateTime.now();
        return current.isAfter(getExpiredAt());
    }

    @Override
    public String toString() {
        return "Post{" +
                " id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", color='" + color + '\'' +
                ", kilometers=" + kilometers +
                ", bodyType='" + bodyType + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", transmission='" + transmission + '\'' +
                ", year=" + year +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", createdAt=" + createdAt +
                ", expiredAt=" + expiredAt +
                ", active=" + active +
                ", postCreatorEmail='" + postCreatorEmail + '\'' +
                ", postCreatorName='" + postCreatorName + '\'' +
                '}';
    }
}
