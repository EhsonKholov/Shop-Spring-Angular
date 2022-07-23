package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthDto {
    @NotNull
    @Size(min = 3, max = 25)
    private String email;
    @NotNull
    @Size(min = 3, max = 25)
    private String password;
}
