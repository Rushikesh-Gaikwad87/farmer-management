package com.farm.service;

import com.farm.dto.FarmerRequestDTO;
import com.farm.dto.FarmerResponseDTO;
import com.farm.exception.DuplicateResourceException;
import com.farm.exception.ResourceNotFoundException;
import com.farm.entity.CropType;
import com.farm.entity.Farmer;
import com.farm.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private FarmerResponseDTO toDTO(Farmer farmer) {
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