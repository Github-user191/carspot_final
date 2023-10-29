package com.carspot.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    private static int EXPIRATION_TIME_HOURS = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column( nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_RESET_TOKEN"))
    private User user;


    public PasswordResetToken(User user, String token) {
        super();
        this.user = user;
        this.token = token;
        this.createdAt = LocalDateTime.now();
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME_HOURS);
    }

    public PasswordResetToken(String token) {
        super();
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME_HOURS);
    }

    private LocalDateTime calculateExpirationDate(int expirationTime) {
        return createdAt.plusHours(expirationTime);
    }


    public boolean isPasswordResetTokenExpired() {
        LocalDateTime current = LocalDateTime.now();
        return current.isAfter(getExpirationTime());
    }
}
