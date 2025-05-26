package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profile(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            model.addAttribute("user", user);
        }
        return "profile";
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateProfile(@RequestParam(required = false) String username,
                                           @RequestParam(required = false) String email,
                                           @RequestParam(required = false) String password,
                                           Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = new User();
            user.setUsername(principal.getName());
            user.setEmail(email);
            user.setPassword(password);
            
            userService.updateProfile(user);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    @PostMapping("/upload-avatar")
    @ResponseBody
    public Map<String, Object> uploadAvatar(@RequestParam("avatar") MultipartFile file, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            String avatarUrl = userService.uploadAvatar(file);
            User user = new User();
            user.setUsername(principal.getName());
            user.setAvatar(avatarUrl);
            userService.updateProfile(user);
            
            response.put("success", true);
            response.put("avatarUrl", avatarUrl);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    @PostMapping("/generate-avatar")
    @ResponseBody
    public Map<String, Object> generateAvatar(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            String avatarUrl = userService.generateNewAvatar();
            User user = new User();
            user.setUsername(principal.getName());
            user.setAvatar(avatarUrl);
            userService.updateProfile(user);
            
            response.put("success", true);
            response.put("avatarUrl", avatarUrl);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
} 