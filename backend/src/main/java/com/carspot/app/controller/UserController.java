package com.carspot.app.controller;

import com.carspot.app.dto.UserAndPostDTO;
import com.carspot.app.entity.User;
import com.carspot.app.payload.request.UpdateAccountDetailsRequest;
import com.carspot.app.service.FileUploadService;
import com.carspot.app.service.UserService;
import com.carspot.app.service.impl.ErrorValidationServiceImpl;
import com.carspot.app.service.impl.FileUploadServiceImpl;
import com.carspot.app.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ErrorValidationServiceImpl errorValidationService;

    public UserController(UserService userService, ErrorValidationServiceImpl errorValidationService) {
        this.userService = userService;
        this.errorValidationService = errorValidationService;
    }



    @GetMapping("/info")
    public ResponseEntity<UserAndPostDTO> getUserAndPostInfo(Principal principal) {
        UserAndPostDTO theUser = userService.getUserAndPostInfo(principal.getName());

        return ResponseEntity.ok(theUser);
    }

    @GetMapping("/info/{emailAddress}")
    public ResponseEntity<UserAndPostDTO> getUserAndPostInfoByEmailAddress(@PathVariable String emailAddress) {
        UserAndPostDTO theUser = userService.getUserAndPostInfo(emailAddress);

        return ResponseEntity.ok(theUser);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUserDetails(@Valid @RequestPart(value="user") UpdateAccountDetailsRequest user, BindingResult result,
                                               @RequestPart(value = "avatar", required = false) MultipartFile avatar, Principal principal) throws IOException {
        ResponseEntity<?> errorMap = errorValidationService.validationService(result);
        if(errorMap != null) return errorMap;


        userService.updateUser(user, avatar);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }


}
