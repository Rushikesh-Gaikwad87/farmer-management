package com.farm.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "farmers")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone", unique = true, length = 15)
    private String phone;

    @Column(name = "village", length = 100)
    private String village;

    @Column(name = "state", length = 50)
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(name = "crop_type")
    private CropType cropType;

    @Column(name = "land_acres", precision = 10, scale = 2)
    private BigDecimal landAcres;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
