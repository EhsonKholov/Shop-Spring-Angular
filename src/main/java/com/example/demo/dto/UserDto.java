package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.io.Serializable;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {

    @NotNull
    @NotEmpty(message = "User name is required")
    @Min(3)
    @Max(20)
    private String userName;

    @NotNull
    @Min(3)
    @Max(20)
    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotNull
    @Min(3)
    @Max(20)
    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotNull
    @Min(3)
    @Max(20)
    @NotEmpty(message = "Email name is required")
    @Email
    private String email;

    @NotNull
    @Min(3)
    @Max(20)
    @NotEmpty(message = "Password name is required")
    private String password;

    @NotNull
    @Min(3)
    @Max(20)
    @NotEmpty(message = "Confirm name is required")
    private String confirmPassword;
}
