package com.example.controller;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.example.entity.User;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            model.addAttribute("username", username);
            userService.findByUsername(username).ifPresent(user -> {
                model.addAttribute("user", user);
            });
            model.addAttribute("isFromLogin", true);
            return "dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/guest-login")
    public String guestLogin() {
        try {
            // 使用AuthenticationManager进行固定访客账号认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    "guest", // 固定访客用户名
                    "guest"  // 固定访客密码
                )
            );
            // 认证成功，设置安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 重定向到主页面
            return "redirect:/dashboard";
        } catch (Exception e) {
            // 认证失败，重定向回登录页并带上错误标记
            // 可以打印日志方便调试
            System.err.println("访客登录失败: " + e.getMessage());
            return "redirect:/login?error=true";
        }
    }
} 