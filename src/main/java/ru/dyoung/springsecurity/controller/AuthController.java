package ru.dyoung.springsecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.dyoung.springsecurity.entity.User;
import ru.dyoung.springsecurity.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String getSignupPage(@ModelAttribute("user") User user) {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String createNewUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("registration fail");
            return "auth/signup";
        }
        userService.addUser(user);
        log.info("registration success");
        return "redirect:/auth/login";
    }

    @GetMapping("/{code}")
    public String activate(@PathVariable String code) {
        log.info("activation user");
        userService.activateUser(code);
        return "auth/login";
    }

    @GetMapping("/email")
    public String getUpdatePage(@ModelAttribute("user") User user) {
        return "auth/email";
    }

    @PostMapping ("/password")
    public String sentCode(@ModelAttribute("user") User user) {
//        if (bindingResult.hasErrors()) {
//            log.error("update fail email");
//            return "email";
//        }
        userService.sentCode(user);
        log.info("registration success");
        return "auth/update";
    }

    @PostMapping ("/update")
    public String updatePassword(@ModelAttribute("user") User user) {
//        if (bindingResult.hasErrors()) {
//            log.error("update fail");
//            return "update";
//        }
        userService.updatePassword(user);
        log.info("registration success");
        return "auth/login";
    }
}
