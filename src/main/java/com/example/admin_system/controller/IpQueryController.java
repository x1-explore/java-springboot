package com.example.admin_system.controller;

import com.example.admin_system.entity.IpInfo;
import com.example.admin_system.service.IpQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IpQueryController {
    
    @Autowired
    private IpQueryService ipQueryService;
    
    @GetMapping("/ip-query")
    public String ipQueryPage(@RequestParam(required = false) String ip, Model model) {
        model.addAttribute("ip", ip);
        return "ip-query";
    }
} 