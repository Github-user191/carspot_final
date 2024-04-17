package com.carspot.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Required")
    private String fullName;

    @NotBlank(message = "Required")
    @Email(message = "Email is invalid")
    @Column(unique = true)
    private String emailAddress;

    @NotBlank(message = "Required")
    @Size(min=11, message = "Invalid")
    private String mobileNumber;

    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(nullable = false, updatable = false)
    private Date dateJoined;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @NotBlank(message = "Required")
    @Column(length = 60)
    private String password;

    // Won't be saved to DB, transient variable
    @Transient
    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = true)
    private int totalActivePostCount = 0;
    @Column(nullable = true)
    private int totalPostCount  = 0;
    @Column(nullable = true)
    private int totalWatchlistPostCount  = 0;

    public User(String fullName, String emailAddress, String mobileNumber, String password) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.dateJoined = new Date();
    }


    public User(long id, String fullName, String emailAddress, String mobileNumber, String password) {
        this.id = id;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.dateJoined = new Date();
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<ContactForm> contactForms = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Review review;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private UserAvatar avatar;



    @PrePersist
    protected void onCreate() {
        this.dateJoined = new Date();
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", dateJoined=" + dateJoined +
                ", emailVerified=" + emailVerified +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", provider=" + provider +
                ", totalActivePostCount=" + totalActivePostCount +
                ", totalPostCount=" + totalPostCount +
                ", totalWatchlistPostCount=" + totalWatchlistPostCount +
                '}';
    }
}
