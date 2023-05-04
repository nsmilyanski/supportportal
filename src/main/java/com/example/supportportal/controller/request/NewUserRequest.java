package com.example.supportportal.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

    private String currentUsername;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String role;

    @NotNull
    private boolean isNotLocked;

    @NotNull
    private boolean active;

    MultipartFile profileImage;
}
