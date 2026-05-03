package com.farm.dto;

import com.farm.entity.CropType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FarmerResponseDTO {

    private Long id;
    private String name;
    private String phone;
    private String village;
    private String state;
    private CropType cropType;
    private BigDecimal landAcres;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    public BigDecimal getLandAcres() {
        return landAcres;
    }

    public void setLandAcres(BigDecimal landAcres) {
        this.landAcres = landAcres;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}