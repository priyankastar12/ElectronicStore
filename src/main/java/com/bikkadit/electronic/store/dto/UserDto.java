package com.bikkadit.electronic.store.dto;

import com.bikkadit.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;


    @Size(min = 3,max = 20,message="Invalid Name !!")
    private String name;


    @NotBlank(message = "Email is required !!")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email !!")
    private String email;

    @NotBlank(message = "Password is required !!")

    private String password;

    @Size(min = 3,max = 6, message = "Invalid gender !!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;

    @ImageNameValid
    private String imageName;
}
