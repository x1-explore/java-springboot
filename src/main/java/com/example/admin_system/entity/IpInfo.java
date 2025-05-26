package com.example.admin_system.entity;

import lombok.Data;

@Data
public class IpInfo {
    private String query;        // IP地址
    private String country;      // 国家
    private String regionName;   // 地区
    private String city;         // 城市
    private String isp;          // ISP提供商
    private String org;          // 组织
    private String as;           // AS编号
    private String status;       // 查询状态
    private String message;      // 错误信息
} 