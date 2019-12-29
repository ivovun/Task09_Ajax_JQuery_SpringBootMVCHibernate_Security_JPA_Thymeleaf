package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.User;
import com.websystique.springmvc.service.UserProfileService;
import com.websystique.springmvc.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AppRestController {

    UserService userService;

    UserProfileService userProfileService;

    MessageSource messageSource;

    public AppRestController(UserService userService, UserProfileService userProfileService,
                         MessageSource messageSource) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.messageSource = messageSource;
    }

    @GetMapping("/api/list")
    public ResponseEntity<List<User>> getCompanyList() {
        return new ResponseEntity(userService.findAll(), HttpStatus.OK);
    }



}
