package com.farm.dto;

import java.math.BigDecimal;

public class StateSummaryDTO {

    private String state;
    private Long farmerCount;
    private BigDecimal totalLandAcres;

    public StateSummaryDTO(String state, Long farmerCount, BigDecimal totalLandAcres) {
        this.state = state;
        this.farmerCount = farmerCount;
        this.totalLandAcres = totalLandAcres;
    }

    public String getState() { return state; }
    public Long getFarmerCount() { return farmerCount; }
    public BigDecimal getTotalLandAcres() { return totalLandAcres; }
}
