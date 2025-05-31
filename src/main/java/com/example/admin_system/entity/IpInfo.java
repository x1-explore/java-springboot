package com.example.admin_system.entity;

import lombok.Data;

@Data
public class IpInfo {
    private String query;        // IP地址
    private String status;       // 查询状态
    private String message;      // 错误信息
    private String country;      // 国家
    private String countryCode;  // 国家代码
    private String regionName;   // 地区/省份
    private String city;         // 城市
    private String district;     // 县/区
    private String postalCode;   // 邮政编码
    private Double latitude;     // 纬度
    private Double longitude;    // 经度
    private String timezone;     // 时区
    private String isp;          // ISP提供商
    private String org;          // 组织/公司
    private String as;           // AS编号
    private String asname;       // AS名称
    private String usageType;    // 用途类型 (如ISP, Hosting, Business)
    private Boolean isProxy;     // 是否代理
    private Boolean isHosting;   // 是否托管
    private Boolean isMobile;    // 是否移动网络
    private String continent;    // 大洲
    private String continentCode;// 大洲代码
    private String currency;     // 货币
} 