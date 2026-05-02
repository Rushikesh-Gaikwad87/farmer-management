package com.farm.service;

import com.farm.entity.CropType;
import com.farm.entity.Farmer;
import com.farm.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmerService {

    private final FarmerRepository farmerRepository;

    // CREATE
    @Transactional
    public Farmer createFarmer(Farmer farmer) {
        if (farmerRepository.existsByPhone(farmer.getPhone())) {
            throw new RuntimeException("Phone number already registered: "
                    + farmer.getPhone());
        }
        return farmerRepository.save(farmer);
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    // READ ONE
    @Transactional(readOnly = true)
    public Farmer getFarmerById(Long id) {
        return farmerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farmer not found with id: " + id));
    }

    // UPDATE
    @Transactional
    public Farmer updateFarmer(Long id, Farmer updatedFarmer) {
        Farmer existing = getFarmerById(id);
        existing.setName(updatedFarmer.getName());
        existing.setPhone(updatedFarmer.getPhone());
        existing.setVillage(updatedFarmer.getVillage());
        existing.setState(updatedFarmer.getState());
        existing.setCropType(updatedFarmer.getCropType());
        existing.setLandAcres(updatedFarmer.getLandAcres());
        return farmerRepository.save(existing);
    }

    // DELETE
    @Transactional
    public void deleteFarmer(Long id) {
        Farmer existing = getFarmerById(id);
        farmerRepository.delete(existing);
    }

    // SEARCH BY STATE
    @Transactional(readOnly = true)
    public List<Farmer> getFarmersByState(String state) {
        return farmerRepository.findByState(state);
    }

    // SEARCH BY CROP
    @Transactional(readOnly = true)
    public List<Farmer> getFarmersByCrop(CropType cropType) {
        return farmerRepository.findByCropType(cropType);
    }
}