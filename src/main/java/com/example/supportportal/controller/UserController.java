package com.example.supportportal.controller;

import com.example.supportportal.controller.request.NewUserRequest;
import com.example.supportportal.exception.ExceptionHandling;
import com.example.supportportal.exception.domain.EmailExistException;
import com.example.supportportal.exception.domain.EmailNotFoundException;
import com.example.supportportal.exception.domain.UserNotFoundException;
import com.example.supportportal.exception.domain.UsernameExistException;
import com.example.supportportal.model.HttpResponse;
import com.example.supportportal.model.User;
import com.example.supportportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static com.example.supportportal.constant.FileConstant.*;

@RestController
@RequestMapping( path = {"/","/users"})
@RequiredArgsConstructor
public class UserController extends ExceptionHandling {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> addNewUser(@RequestBody NewUserRequest request) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
        return new ResponseEntity<>(userService.addNewUser(request), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateNewUser(@RequestBody NewUserRequest request) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
        return new ResponseEntity<>(userService.updateUser(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<User> updateProfileImage(@RequestParam("username") String username, @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
        User user = userService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(user, OK);
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.findUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping(path = "/image/{username}/{filename}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH));
    }

    @GetMapping(path = "/image/profile/{filename}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try(InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];

            while ((bytesRead = inputStream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }
        }
        return outputStream.toByteArray();
    }



    @PatchMapping("/resetPassword/{email}}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws EmailNotFoundException {
        userService.resetPassword(email);
        return new ResponseEntity<>(new HttpResponse(new Date(), HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.getReasonPhrase()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new HttpResponse(new Date(), HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.getReasonPhrase()), HttpStatus.OK);
    }

}
