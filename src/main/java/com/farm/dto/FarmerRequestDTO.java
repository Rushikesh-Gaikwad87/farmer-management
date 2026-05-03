package com.farm.dto;

import com.farm.entity.CropType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class FarmerRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be exactly 10 digits")
    private String phone;

    @Size(max = 100, message = "Village name cannot exceed 100 characters")
    private String village;

    @Size(max = 50, message = "State name cannot exceed 50 characters")
    private String state;

    @NotNull(message = "Crop type is required")
    private CropType cropType;

    @NotNull(message = "Land acres is required")
    @DecimalMin(value = "0.1", message = "Land acres must be at least 0.1")
    @DecimalMax(value = "99999999.99", message = "Land acres value is too large")
    private BigDecimal landAcres;

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
}