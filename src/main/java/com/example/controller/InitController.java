package com.example.controller;

import com.example.entity.SystemInit;
import com.example.entity.User;
import com.example.repository.SystemInitRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InitController {

    @Autowired
    private SystemInitRepository systemInitRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String root() {
        // 如果系统已初始化，跳转到登录页
        if (systemInitRepository.existsByInitializedTrue()) {
            return "redirect:/login";
        }
        // 如果系统未初始化，跳转到初始化页
        return "redirect:/init";
    }

    @GetMapping("/init")
    public String showInitPage(Model model) {
        // 如果系统已初始化，跳转到登录页
        if (systemInitRepository.existsByInitializedTrue()) {
            return "redirect:/login";
        }
        return "init";
    }

    @PostMapping("/init")
    public String initSystem(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model) {
        // 如果系统已初始化，跳转到登录页
        if (systemInitRepository.existsByInitializedTrue()) {
            return "redirect:/login";
        }

        try {
            // 创建管理员账号
            User admin = userService.createAdminUser(username, password, email);

            // 标记系统已初始化
            SystemInit init = new SystemInit();
            init.setInitialized(true);
            systemInitRepository.save(init);

            model.addAttribute("success", "管理员账号创建成功！");
            return "success";
        } catch (Exception e) {
            model.addAttribute("error", "初始化失败：" + e.getMessage());
            return "init";
        }
    }
} 