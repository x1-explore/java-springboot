package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public String searchPage(@RequestParam(required = false) String keyword, 
                           @AuthenticationPrincipal User currentUser,
                           Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<User> users = userService.searchUsers(keyword, currentUser);
            model.addAttribute("users", users);
        }
        model.addAttribute("keyword", keyword);
        return "user/search";
    }

    @GetMapping("/api/users/search")
    @ResponseBody
    public List<User> searchUsersApi(@RequestParam String keyword, @AuthenticationPrincipal User currentUser) {
        return userService.searchUsers(keyword, currentUser);
    }
} 