package com.example.admin_system.controller;

import com.example.admin_system.entity.IpInfo;
import com.example.admin_system.service.IpQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ip-query")
public class IpQueryApiController {
    
    @Autowired
    private IpQueryService ipQueryService;
    
    @GetMapping("/api1")
    public CompletableFuture<IpInfo> queryIpApi1(@RequestParam(required = false) String ip) {
        return ipQueryService.queryIpApi1(ip);
    }
    
    @GetMapping("/api2")
    public CompletableFuture<IpInfo> queryIpApi2(@RequestParam(required = false) String ip) {
        return ipQueryService.queryIpApi2(ip);
    }
} 