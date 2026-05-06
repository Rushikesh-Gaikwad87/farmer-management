package com.farm.controller;

import com.farm.dto.FarmerRequestDTO;
import com.farm.dto.FarmerResponseDTO;
import com.farm.dto.PagedResponseDTO;
import com.farm.dto.StateSummaryDTO;
import com.farm.entity.CropType;
import com.farm.service.FarmerService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Farmer API", description = "CRUD operations and search for farmers")
@SuppressWarnings("unused")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @Operation(summary = "Get all farmers", description = "Returns all farmers without pagination")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    @GetMapping
    public ResponseEntity<List<FarmerResponseDTO>> getAllFarmers() {
        return ResponseEntity.ok(farmerService.getAllFarmers());
    }

    @Operation(summary = "Get farmer by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Farmer found"),
            @ApiResponse(responseCode = "404", description = "Farmer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FarmerResponseDTO> getFarmerById(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerById(id));
    }

    @Operation(summary = "Get farmers by state", description = "Returns all farmers from a specific state")
    @ApiResponse(responseCode = "200", description = "State farmer list returned successfully")
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
    @Operation(summary = "Search farmers by name", description = "Case-insensitive partial search by farmer name")
    @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    @GetMapping("/search")
    public ResponseEntity<List<FarmerResponseDTO>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(farmerService.searchByName(name));
    }

    // GET /api/farmers/land-range?min=5&max=20
    @Operation(summary = "Get farmers by land range", description = "Returns farmers whose land acres fall between the given values")
    @ApiResponse(responseCode = "200", description = "Land range results returned successfully")
    @GetMapping("/land-range")
    public ResponseEntity<List<FarmerResponseDTO>> getByLandRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(farmerService.getFarmersByLandRange(min, max));
    }

    // GET /api/farmers/filter?state=Maharashtra&cropType=WHEAT
    @Operation(summary = "Filter farmers by state and crop type", description = "Returns farmers matching both state and crop type")
    @ApiResponse(responseCode = "200", description = "Filtered farmers returned successfully")
    @GetMapping("/filter")
    public ResponseEntity<List<FarmerResponseDTO>> getByStateAndCrop(
            @RequestParam String state,
            @RequestParam CropType cropType) {
        return ResponseEntity.ok(farmerService.getFarmersByStateAndCrop(state, cropType));
    }

    // GET /api/farmers/summary
    @Operation(summary = "Get state summary", description = "Returns farmer count and total land acres per state")
    @ApiResponse(responseCode = "200", description = "State summary returned successfully")
    @GetMapping("/summary")
    public ResponseEntity<List<StateSummaryDTO>> getStateSummary() {
        return ResponseEntity.ok(farmerService.getStateSummary());
    }

    @Operation(summary = "Create a new farmer", description = "Creates farmer with validation. Phone must be unique.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Farmer created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Phone number already registered")
    })
    @PostMapping
    public ResponseEntity<FarmerResponseDTO> createFarmer(@Valid @RequestBody FarmerRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(farmerService.createFarmer(dto));
    }

    @Operation(summary = "Update farmer", description = "Updates an existing farmer by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Farmer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Farmer not found"),
            @ApiResponse(responseCode = "409", description = "Phone number already registered")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FarmerResponseDTO> updateFarmer(
            @PathVariable Long id,
            @Valid @RequestBody FarmerRequestDTO dto) {
        return ResponseEntity.ok(farmerService.updateFarmer(id, dto));
    }

    @Operation(summary = "Delete farmer", description = "Deletes a farmer by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Farmer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Farmer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable Long id) {
        farmerService.deleteFarmer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get farmers by crop type", description = "Returns farmers for a specific crop type")
    @ApiResponse(responseCode = "200", description = "Crop farmer list returned successfully")
    @GetMapping("/crop/{cropType}")
    public ResponseEntity<List<FarmerResponseDTO>> getFarmersByCrop(@PathVariable CropType cropType) {
        return ResponseEntity.ok(farmerService.getFarmersByCrop(cropType));
    }
}
