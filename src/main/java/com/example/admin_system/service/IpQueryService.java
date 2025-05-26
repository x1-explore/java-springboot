package com.example.admin_system.service;

import com.example.admin_system.entity.IpInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@Service
public class IpQueryService {
    private final String API_URL_1 = "http://ip-api.com/json/";
    private final String API_URL_2 = "https://ipapi.co/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private HttpServletRequest request;
    
    private String getClientIp() {
        String ip = null;
        
        // 1. 尝试从X-Real-IP获取
        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) return ip;
        
        // 2. 尝试从X-Forwarded-For获取
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() > 0) {
            // 获取第一个IP
            ip = ip.split(",")[0].trim();
            if (isValidIp(ip)) return ip;
        }
        
        // 3. 尝试从其他代理头获取
        String[] proxyHeaders = {
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
        };
        
        for (String header : proxyHeaders) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) return ip;
        }
        
        // 4. 最后尝试从RemoteAddr获取
        ip = request.getRemoteAddr();
        if (isValidIp(ip)) return ip;
        
        return "127.0.0.1"; // 如果都获取不到，返回本地IP
    }
    
    private boolean isValidIp(String ip) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        // 简单的IP格式验证
        return ip.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
    }
    
    @Async
    public CompletableFuture<IpInfo> queryIpApi1(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String queryIp = ip != null ? ip : getClientIp();
        String url = API_URL_1 + queryIp;
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<IpInfo> response = restTemplate.exchange(url, HttpMethod.GET, entity, IpInfo.class);
            return CompletableFuture.completedFuture(response.getBody());
        } catch (Exception e) {
            IpInfo errorInfo = new IpInfo();
            errorInfo.setStatus("fail");
            errorInfo.setMessage("API1查询失败：" + e.getMessage());
            return CompletableFuture.completedFuture(errorInfo);
        }
    }

    @Async
    public CompletableFuture<IpInfo> queryIpApi2(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String queryIp = ip != null ? ip : getClientIp();
        String url = API_URL_2 + queryIp + "/json/";
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            Map<String, Object> data = objectMapper.readValue(response.getBody(), Map.class);
            
            IpInfo ipInfo = new IpInfo();
            ipInfo.setStatus("success");
            ipInfo.setQuery(queryIp);
            ipInfo.setCountry((String) data.get("country_name"));
            ipInfo.setRegionName((String) data.get("region"));
            ipInfo.setCity((String) data.get("city"));
            ipInfo.setIsp((String) data.get("org"));
            ipInfo.setOrg((String) data.get("org"));
            
            return CompletableFuture.completedFuture(ipInfo);
        } catch (Exception e) {
            IpInfo errorInfo = new IpInfo();
            errorInfo.setStatus("fail");
            errorInfo.setMessage("API2查询失败：" + e.getMessage());
            return CompletableFuture.completedFuture(errorInfo);
        }
    }
} 