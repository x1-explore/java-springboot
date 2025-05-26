package com.example.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "system_init")
public class SystemInit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private boolean initialized = false;
} 