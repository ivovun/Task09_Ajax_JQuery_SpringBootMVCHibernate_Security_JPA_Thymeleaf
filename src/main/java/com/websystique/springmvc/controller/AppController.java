package com.websystique.springmvc.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.websystique.springmvc.model.User;
import com.websystique.springmvc.model.UserProfile;
import com.websystique.springmvc.service.UserProfileService;
import com.websystique.springmvc.service.UserService;

@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {
    UserService userService;

    UserProfileService userProfileService;

    MessageSource messageSource;

    public AppController(UserService userService, UserProfileService userProfileService,
                         MessageSource messageSource) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.messageSource = messageSource;
    }

    /**
     * This method handles login GET requests.
     * If users is already logged-in and tries to goto login page again, will be redirected to list page.
     */
    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
    public String loginPage() {
        if (isCurrentAuthenticationAnonymous()) {
            return "login";
        } else {
            return "redirect:/admin/list";
        }
    }

    /**
     * This method handles logout requests.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value = {"/user"}, method = RequestMethod.GET)
    public String userPage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipal());
        model.addAttribute("showAdminPanel", false);
        return "userslist";
    }

    /**
     * This method will list all existing users.
     */
    @RequestMapping(value = {"/admin/list"}, method = RequestMethod.GET)
    public String listUsers(ModelMap model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("loggedinuser", getPrincipal());
        model.addAttribute("showAdminPanel", true);

        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("newUserRegistration", true);

        return "userslist";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * saving user in database. It also validates the user input
     */
    @PostMapping(value = {"/admin/newuser"})
    public String newUser(@Valid @ModelAttribute("user") User user, BindingResult result, ModelMap model) {
        /*
         * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation
         * and applying it on field [sso] of Model class [User].
         *
         * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
         * framework as well while still using internationalized messages.
         *
         */
        if ((result.hasErrors()) || (!userService.isUserSSOUnique(user.getId(), user.getSsoId()))) {
            FieldError ssoError = new FieldError("user", "ssoId", messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
            result.addError(ssoError);
            model.addAttribute("user", user);
            model.addAttribute("newUserRegistration", true);
            return "error";
        }

        userService.save(user);
        return "redirect:/admin/list";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @PostMapping("/admin/save")
    public String save(@ModelAttribute("user") User user, BindingResult result, ModelMap model) {
        /*
         * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation
         * and applying it on field [sso] of Model class [User].
         *
         * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
         * framework as well while still using internationalized messages.
         *
         */
        if ((result.hasErrors()) || (!userService.isUserSSOUnique(user.getId(), user.getSsoId()))) {
            FieldError ssoError = new FieldError("user", "ssoId", messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
            result.addError(ssoError);
            model.addAttribute("user", user);
            model.addAttribute("newUserRegistration", false);
            return "error";
        }

        userService.save(user);
        return "redirect:/admin/list";
    }

    /**
     * This method will delete an user by it's SSOID value.
     */
    @GetMapping(value = {"/admin/delete"})
    public String delete(@RequestParam("ssoId") String ssoId) {
        userService.deleteUserBySsoId(ssoId);
        return "redirect:/admin/list";
    }

    /**
     * This method will provide UserProfile list to views
     */
    @ModelAttribute("roles")
    public List<UserProfile> initializeProfiles() {
        return userProfileService.findAll();
    }

    /**
     * This method handles Access-Denied redirect.
     */
    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipal());
        return "accessDenied";
    }

    /**
     * This method returns the principal[user-name] of logged-in user.
     */
    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    /**
     * This method returns true if users is already authenticated [logged-in], else false.
     */
    private boolean isCurrentAuthenticationAnonymous() {
        return SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken;
    }
}