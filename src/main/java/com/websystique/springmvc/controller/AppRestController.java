package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.User;
import com.websystique.springmvc.service.UserProfileService;
import com.websystique.springmvc.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/admin/api/list")
    public ResponseEntity<List<User>> getCompanyList() {
        return new ResponseEntity(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/admin/api/edit")
    public ResponseEntity<User> edit(@RequestParam("id") String id, ModelMap model) {
        return new ResponseEntity(userService.findById(Long.parseLong(id)), HttpStatus.OK);
    }

    @PostMapping("/admin/api/save")
    public ResponseEntity<Void> save(@RequestBody User user) {
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/api/delete/{ssoId}")
    public ResponseEntity<Void> delete(@PathVariable String ssoId) {
        userService.deleteUserBySsoId(ssoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
