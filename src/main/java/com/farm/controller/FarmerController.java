package com.farm.controller;

import com.farm.dto.FarmerRequestDTO;
import com.farm.dto.FarmerResponseDTO;
import com.farm.dto.PagedResponseDTO;
import com.farm.dto.StateSummaryDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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

    // GET /api/farmers/paged?page=0&size=10&sortBy=name&direction=asc
    @GetMapping("/paged")
    public ResponseEntity<PagedResponseDTO<FarmerResponseDTO>> getAllFarmersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(
            farmerService.getAllFarmersPaged(page, size, sortBy, direction));
    }

    // GET /api/farmers/state/{state}/paged?page=0&size=5
    @GetMapping("/state/{state}/paged")
    public ResponseEntity<PagedResponseDTO<FarmerResponseDTO>> getFarmersByStatePaged(
            @PathVariable String state,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(
            farmerService.getFarmersByStatePaged(state, page, size));
    }

    // CUSTOM JPQL ENDPOINTS

    // GET /api/farmers/search?name=kumar
    @GetMapping("/search")
    public ResponseEntity<List<FarmerResponseDTO>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(farmerService.searchByName(name));
    }

    // GET /api/farmers/land-range?min=5&max=20
    @GetMapping("/land-range")
    public ResponseEntity<List<FarmerResponseDTO>> getByLandRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(farmerService.getFarmersByLandRange(min, max));
    }

    // GET /api/farmers/filter?state=Maharashtra&cropType=WHEAT
    @GetMapping("/filter")
    public ResponseEntity<List<FarmerResponseDTO>> getByStateAndCrop(
            @RequestParam String state,
            @RequestParam CropType cropType) {
        return ResponseEntity.ok(farmerService.getFarmersByStateAndCrop(state, cropType));
    }

    // GET /api/farmers/summary
    @GetMapping("/summary")
    public ResponseEntity<List<StateSummaryDTO>> getStateSummary() {
        return ResponseEntity.ok(farmerService.getStateSummary());
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
