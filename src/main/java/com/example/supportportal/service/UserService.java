package com.example.supportportal.service;


import com.example.supportportal.controller.request.NewUserRequest;
import com.example.supportportal.exception.domain.EmailExistException;
import com.example.supportportal.exception.domain.EmailNotFoundException;
import com.example.supportportal.exception.domain.UserNotFoundException;
import com.example.supportportal.exception.domain.UsernameExistException;
import com.example.supportportal.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException;

    List<User> getUsers();

    User findUserByUsername(String username);
    User findUserByEmail(String email);

    User addNewUser(NewUserRequest request) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
    User updateUser(NewUserRequest request) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;

    void deleteUser(Integer id);

    void resetPassword(String email) throws EmailNotFoundException;

    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
}
