package com.farm.service;

import com.farm.dto.FarmerRequestDTO;
import com.farm.dto.FarmerResponseDTO;
import com.farm.dto.PagedResponseDTO;
import com.farm.dto.StateSummaryDTO;
import com.farm.exception.DuplicateResourceException;
import com.farm.exception.ResourceNotFoundException;
import com.farm.entity.CropType;
import com.farm.entity.Farmer;
import com.farm.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmerService {

    private final FarmerRepository farmerRepository;

    // CREATE
    @Transactional
    public FarmerResponseDTO createFarmer(FarmerRequestDTO dto) {
        if (farmerRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateResourceException("Phone number already registered: "
                    + dto.getPhone());
        }
        Farmer saved = farmerRepository.save(toEntity(dto));
        return toDTO(saved);
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<FarmerResponseDTO> getAllFarmers() {
        return farmerRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // READ ONE
    @Transactional(readOnly = true)
    public FarmerResponseDTO getFarmerById(Long id) {
        return toDTO(farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + id)));
    }

    // UPDATE
    @Transactional
    public FarmerResponseDTO updateFarmer(Long id, FarmerRequestDTO dto) {
        Farmer existing = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + id));
        if (!existing.getPhone().equals(dto.getPhone()) && farmerRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateResourceException("Phone number already registered: " + dto.getPhone());
        }
        existing.setName(dto.getName());
        existing.setPhone(dto.getPhone());
        existing.setVillage(dto.getVillage());
        existing.setState(dto.getState());
        existing.setCropType(dto.getCropType());
        existing.setLandAcres(dto.getLandAcres());
        return toDTO(farmerRepository.save(existing));
    }

    // DELETE
    @Transactional
    public void deleteFarmer(Long id) {
        Farmer existing = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + id));
        farmerRepository.delete(existing);
    }

    // SEARCH BY STATE
    @Transactional(readOnly = true)
    public List<FarmerResponseDTO> getFarmersByState(String state) {
        return farmerRepository.findByState(state)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // SEARCH BY CROP
    @Transactional(readOnly = true)
    public List<FarmerResponseDTO> getFarmersByCrop(CropType cropType) {
        return farmerRepository.findByCropType(cropType)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // PAGINATED + SORTED LIST OF ALL FARMERS
    @Transactional(readOnly = true)
    public PagedResponseDTO<FarmerResponseDTO> getAllFarmersPaged(
            int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Farmer> farmerPage = farmerRepository.findAll(pageable);

        List<FarmerResponseDTO> content = farmerPage.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PagedResponseDTO<>(
                content,
                farmerPage.getNumber(),
                farmerPage.getSize(),
                farmerPage.getTotalElements(),
                farmerPage.getTotalPages(),
                farmerPage.isLast()
        );
    }

    // PAGINATED SEARCH BY STATE
    @Transactional(readOnly = true)
    public PagedResponseDTO<FarmerResponseDTO> getFarmersByStatePaged(
            String state, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Farmer> farmerPage = farmerRepository.findByState(state, pageable);

        List<FarmerResponseDTO> content = farmerPage.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PagedResponseDTO<>(
                content,
                farmerPage.getNumber(),
                farmerPage.getSize(),
                farmerPage.getTotalElements(),
                farmerPage.getTotalPages(),
                farmerPage.isLast()
        );
    }

    // CUSTOM JPQL QUERIES

    // Search farmers by name keyword
    @Transactional(readOnly = true)
    public List<FarmerResponseDTO> searchByName(String name) {
        return farmerRepository.searchByName(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Find farmers by land range
    @Transactional(readOnly = true)
    public List<FarmerResponseDTO> getFarmersByLandRange(
            BigDecimal minAcres, BigDecimal maxAcres) {
        return farmerRepository.findByLandAcresBetween(minAcres, maxAcres)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Find farmers by state and crop
    @Transactional(readOnly = true)
    public List<FarmerResponseDTO> getFarmersByStateAndCrop(
            String state, CropType cropType) {
        return farmerRepository.findByStateAndCrop(state, cropType)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Get state summary — farmer count + total land per state
    @Transactional(readOnly = true)
    public List<StateSummaryDTO> getStateSummary() {
        List<Object[]> countResults = farmerRepository.countFarmersByState();
        List<Object[]> landResults = farmerRepository.totalLandAcresByState();

        // Map land totals by state for easy lookup
        java.util.Map<String, BigDecimal> landMap = new java.util.HashMap<>();
        for (Object[] row : landResults) {
            landMap.put((String) row[0], (BigDecimal) row[1]);
        }

        return countResults.stream()
                .map(row -> new StateSummaryDTO(
                        (String) row[0],
                        (Long) row[1],
                        landMap.getOrDefault((String) row[0], BigDecimal.ZERO)
                ))
                .collect(Collectors.toList());
    }

    private Farmer toEntity(FarmerRequestDTO dto) {
        Farmer farmer = new Farmer();
        farmer.setName(dto.getName());
        farmer.setPhone(dto.getPhone());
        farmer.setVillage(dto.getVillage());
        farmer.setState(dto.getState());
        farmer.setCropType(dto.getCropType());
        farmer.setLandAcres(dto.getLandAcres());
        return farmer;
    }

    protected FarmerResponseDTO toDTO(Farmer farmer) {
        FarmerResponseDTO dto = new FarmerResponseDTO();
        dto.setId(farmer.getId());
        dto.setName(farmer.getName());
        dto.setPhone(farmer.getPhone());
        dto.setVillage(farmer.getVillage());
        dto.setState(farmer.getState());
        dto.setCropType(farmer.getCropType());
        dto.setLandAcres(farmer.getLandAcres());
        dto.setCreatedAt(farmer.getCreatedAt());
        return dto;
    }
}