package com.carspot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactForm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Required")
    private String contactFormFullName;

    @NotBlank(message = "Required")
    @Email(message = "Email is invalid")
    private String contactFormEmailAddress;

    @NotBlank(message = "Required")
    @Size(min = 20,message = "Minimum of 20 characters")
    private String contactFormMessage;

    @NotBlank(message = "Required")
    private String contactFormSubject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    @JsonIgnore
    private User user;






}
