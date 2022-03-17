package ru.dyoung.springsecurity.entity;

import lombok.Data;
import lombok.NonNull;
import ru.dyoung.springsecurity.model.Role;
import ru.dyoung.springsecurity.model.Status;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty")
    private String password;

    @Column(name = "first_name")
    @NotEmpty(message = "Firstname should not be empty")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Lastname should not be empty")
    private String lastName;

    @Column(name = "login")
    @NotEmpty(message = "Login should not be empty")
    private String login;

    @Column(name = "code")
    private String activationCode;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

}