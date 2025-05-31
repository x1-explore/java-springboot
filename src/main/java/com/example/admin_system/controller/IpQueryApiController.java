package com.example.admin_system.controller;

import com.example.admin_system.entity.IpInfo;
import com.example.admin_system.service.IpQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/ip-query")
public class IpQueryApiController {
    
    @Autowired
    private IpQueryService ipQueryService;
    
    @GetMapping("/api1")
    public CompletableFuture<IpInfo> queryIpApi1(@RequestParam(required = false) String ip) {
        return ipQueryService.queryIpApi1(ip);
    }
    
    private static final String API1_URL = "https://ipapi.co/{ip}/json/";
    
    @GetMapping("/api2")
    public CompletableFuture<IpInfo> queryIpApi2(@RequestParam(required = false) String ip) {
        return ipQueryService.queryIpApi2(ip);
    }

    @GetMapping("/api3")
    public CompletableFuture<IpInfo> queryIpApi3(@RequestParam(required = false) String ip) {
        return ipQueryService.queryIpApi3(ip);
    }

    @GetMapping("/api4")
    public CompletableFuture<IpInfo> queryIpApi4(@RequestParam(required = false) String ip) {
        return ipQueryService.queryIpApi4(ip);
    }

    @GetMapping("/api5")
    public CompletableFuture<IpInfo> queryIpApi5(@RequestParam(required = false) String ip) {
        return ipQueryService.queryIpApi5(ip);
    }
    
    private static final String API5_URL = "https://ipbase.com/json/";

    @GetMapping("/api6")
    public CompletableFuture<IpInfo> queryIpApi6(@RequestParam(required = false) String ip) {
        return ipQueryService.queryIpApi6(ip);
    }
    
    @GetMapping("/client-ip")
    public Map<String, String> getClientIp() {
        String ip = ipQueryService.getClientIpForApi();
        return Collections.singletonMap("ip", ip);
    }
}