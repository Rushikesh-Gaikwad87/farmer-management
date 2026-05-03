package com.farm.controller;

import com.farm.dto.FarmerRequestDTO;
import com.farm.dto.FarmerResponseDTO;
import com.farm.entity.CropType;
import com.farm.service.FarmerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
@SuppressWarnings("unused")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @GetMapping
    public ResponseEntity<List<FarmerResponseDTO>> getAllFarmers() {
        return ResponseEntity.ok(farmerService.getAllFarmers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmerResponseDTO> getFarmerById(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerById(id));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<FarmerResponseDTO>> getFarmersByState(@PathVariable String state) {
        return ResponseEntity.ok(farmerService.getFarmersByState(state));
    }

    @PostMapping
    public ResponseEntity<FarmerResponseDTO> createFarmer(@Valid @RequestBody FarmerRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(farmerService.createFarmer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FarmerResponseDTO> updateFarmer(
            @PathVariable Long id,
            @Valid @RequestBody FarmerRequestDTO dto) {
        return ResponseEntity.ok(farmerService.updateFarmer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable Long id) {
        farmerService.deleteFarmer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/crop/{cropType}")
    public ResponseEntity<List<FarmerResponseDTO>> getFarmersByCrop(@PathVariable CropType cropType) {
        return ResponseEntity.ok(farmerService.getFarmersByCrop(cropType));
    }
}
