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
@SuppressWarnings("unchecked")
public class IpQueryService {
    private static final String API1_URL = "https://ip-api.com/json/";
    private static final String API2_URL = "https://ipapi.co/json/";
    private static final String API3_URL = "http://ip-api.com/json/";
    private static final String API4_URL = "https://freegeoip.app/json/";
    private static final String API5_URL = "https://ipwhois.app/json/";
    private static final String API6_URL = "https://ipinfo.io/json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private HttpServletRequest request;
    
    private String getClientIp() {
        String ip = null;
        
        // 1. 优先从X-Forwarded-For获取用户真实IP
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() > 0) {
            // 获取第一个IP（最接近用户的IP）
            ip = ip.split(",")[0].trim();
            if (isValidIp(ip)) return ip;
        }
        
        // 2. 尝试从X-Real-IP获取
        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) return ip;
        
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
        
        // 5. 如果都获取不到，调用外部API获取公网IP
        try {
            RestTemplate restTemplate = new RestTemplate();
            String publicIp = restTemplate.getForObject("https://api.ipify.org?format=json", String.class);
            if (publicIp != null && isValidIp(publicIp)) {
                return publicIp;
            }
        } catch (Exception e) {
            // 忽略异常
        }
        
        return "127.0.0.1"; // 如果都获取不到，返回本地IP
    }
    
    private boolean isValidIp(String ip) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        // 简单的IP格式验证
        return ip.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
    }
    
    public String getClientIpForApi() {
        return getClientIp();
    }
    
    @Async
    public CompletableFuture<IpInfo> queryIpApi1(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String queryIp = ip != null ? ip : getClientIp();
        String url = API1_URL + (queryIp.equals("") ? "" : "?ip=" + queryIp);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            Map<String, Object> data = objectMapper.readValue(response.getBody(), Map.class);
            
            IpInfo ipInfo = new IpInfo();
            ipInfo.setStatus("success");
            ipInfo.setQuery(queryIp);
            ipInfo.setCountry((String) data.get("country"));
            ipInfo.setRegionName((String) data.get("province"));
            ipInfo.setCity((String) data.get("city"));
            ipInfo.setIsp((String) data.get("isp"));
            
            return CompletableFuture.completedFuture(ipInfo);
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
        String url = API2_URL + (queryIp.equals("") ? "" : "?ip=" + queryIp);
        
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

    @Async
    public CompletableFuture<IpInfo> queryIpApi3(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String queryIp = ip != null ? ip : getClientIp();
        String url = API3_URL + (queryIp.equals("") ? "" : "?ip=" + queryIp);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            Map<String, Object> data = objectMapper.readValue(response.getBody(), Map.class);
            
            IpInfo ipInfo = new IpInfo();
            ipInfo.setStatus("success");
            ipInfo.setQuery(queryIp);
            ipInfo.setCountry((String) data.get("country"));
            ipInfo.setRegionName((String) data.get("region"));
            ipInfo.setCity((String) data.get("city"));
            ipInfo.setIsp((String) data.get("isp"));
            ipInfo.setOrg((String) data.get("org"));
            
            return CompletableFuture.completedFuture(ipInfo);
        } catch (Exception e) {
            IpInfo errorInfo = new IpInfo();
            errorInfo.setStatus("fail");
            errorInfo.setMessage("API3查询失败：" + e.getMessage());
            return CompletableFuture.completedFuture(errorInfo);
        }
    }

    @Async
    public CompletableFuture<IpInfo> queryIpApi4(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String queryIp = ip != null ? ip : getClientIp();
        String url = API4_URL + (queryIp.equals("") ? "" : "?ip=" + queryIp);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String jsonStr = response.getBody().replace("var returnCitySN = ", "");
            Map<String, Object> data = objectMapper.readValue(jsonStr, Map.class);
            
            IpInfo ipInfo = new IpInfo();
            ipInfo.setStatus("success");
            ipInfo.setQuery(queryIp);
            ipInfo.setCountry("中国");
            ipInfo.setRegionName((String) data.get("province"));
            ipInfo.setCity((String) data.get("city"));
            ipInfo.setIsp((String) data.get("isp"));
            
            return CompletableFuture.completedFuture(ipInfo);
        } catch (Exception e) {
            IpInfo errorInfo = new IpInfo();
            errorInfo.setStatus("fail");
            errorInfo.setMessage("API4查询失败：" + e.getMessage());
            return CompletableFuture.completedFuture(errorInfo);
        }
    }

    @Async
    public CompletableFuture<IpInfo> queryIpApi5(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String queryIp = ip != null ? ip : getClientIp();
        String url = API5_URL + (queryIp.equals("") ? "" : "?ip=" + queryIp);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("X-Auth-Token", "your-huawei-cloud-token"); // 需要替换为实际token
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            Map<String, Object> data = objectMapper.readValue(response.getBody(), Map.class);
            Map<String, Object> ipData = (Map<String, Object>) data.get("data");
            
            IpInfo ipInfo = new IpInfo();
            ipInfo.setStatus("success");
            ipInfo.setQuery(queryIp);
            ipInfo.setCountry((String) ipData.get("country"));
            ipInfo.setRegionName((String) ipData.get("region"));
            ipInfo.setCity((String) ipData.get("city"));
            ipInfo.setIsp((String) ipData.get("isp"));
            ipInfo.setOrg((String) ipData.get("org"));
            
            return CompletableFuture.completedFuture(ipInfo);
        } catch (Exception e) {
            IpInfo errorInfo = new IpInfo();
            errorInfo.setStatus("fail");
            errorInfo.setMessage("API5查询失败：" + e.getMessage());
            return CompletableFuture.completedFuture(errorInfo);
        }
    }

    @Async
    public CompletableFuture<IpInfo> queryIpApi6(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String queryIp = ip != null ? ip : getClientIp();
        String url = API6_URL + (queryIp.equals("") ? "" : "?ip=" + queryIp);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            Map<String, Object> data = objectMapper.readValue(response.getBody(), Map.class);
            
            IpInfo ipInfo = new IpInfo();
            ipInfo.setStatus("success");
            ipInfo.setQuery(queryIp);
            ipInfo.setCountry((String) data.get("country"));
            ipInfo.setRegionName((String) data.get("province"));
            ipInfo.setCity((String) data.get("city"));
            ipInfo.setIsp((String) data.get("isp"));
            ipInfo.setOrg((String) data.get("org"));
            
            return CompletableFuture.completedFuture(ipInfo);
        } catch (Exception e) {
            IpInfo errorInfo = new IpInfo();
            errorInfo.setStatus("fail");
            errorInfo.setMessage("API6查询失败：" + e.getMessage());
            return CompletableFuture.completedFuture(errorInfo);
        }
    }
}