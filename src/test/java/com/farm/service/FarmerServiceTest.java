package com.farm.service;

import com.farm.dto.FarmerRequestDTO;
import com.farm.dto.FarmerResponseDTO;
import com.farm.entity.CropType;
import com.farm.entity.Farmer;
import com.farm.exception.DuplicateResourceException;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.FarmerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FarmerServiceTest {

    @Mock
    private FarmerRepository farmerRepository;

    @InjectMocks
    private FarmerService farmerService;

    private Farmer farmer;
    private FarmerRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        farmer = new Farmer();
        farmer.setId(1L);
        farmer.setName("Ramesh Kumar");
        farmer.setPhone("9876543210");
        farmer.setVillage("Nashik");
        farmer.setState("Maharashtra");
        farmer.setCropType(CropType.WHEAT);
        farmer.setLandAcres(new BigDecimal("12.50"));

        requestDTO = new FarmerRequestDTO();
        requestDTO.setName("Ramesh Kumar");
        requestDTO.setPhone("9876543210");
        requestDTO.setVillage("Nashik");
        requestDTO.setState("Maharashtra");
        requestDTO.setCropType(CropType.WHEAT);
        requestDTO.setLandAcres(new BigDecimal("12.50"));
    }

    @Test
    void createFarmer_Success() {
        when(farmerRepository.existsByPhone("9876543210")).thenReturn(false);
        when(farmerRepository.save(any(Farmer.class))).thenReturn(farmer);

        FarmerResponseDTO result = farmerService.createFarmer(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Ramesh Kumar");
        assertThat(result.getPhone()).isEqualTo("9876543210");
        assertThat(result.getState()).isEqualTo("Maharashtra");

        verify(farmerRepository, times(1)).save(any(Farmer.class));
    }

    @Test
    void createFarmer_DuplicatePhone_ThrowsException() {
        when(farmerRepository.existsByPhone("9876543210")).thenReturn(true);

        assertThatThrownBy(() -> farmerService.createFarmer(requestDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("9876543210");

        verify(farmerRepository, never()).save(any(Farmer.class));
    }

    @Test
    void getFarmerById_Found() {
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));

        FarmerResponseDTO result = farmerService.getFarmerById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Ramesh Kumar");
    }

    @Test
    void getFarmerById_NotFound_ThrowsException() {
        when(farmerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> farmerService.getFarmerById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getAllFarmers_ReturnsList() {
        when(farmerRepository.findAll()).thenReturn(List.of(farmer));

        List<FarmerResponseDTO> result = farmerService.getAllFarmers();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Ramesh Kumar");
    }

    @Test
    void getAllFarmers_EmptyList() {
        when(farmerRepository.findAll()).thenReturn(List.of());

        List<FarmerResponseDTO> result = farmerService.getAllFarmers();

        assertThat(result).isEmpty();
    }

    @Test
    void updateFarmer_Success() {
        FarmerRequestDTO updateDTO = new FarmerRequestDTO();
        updateDTO.setName("Ramesh Updated");
        updateDTO.setPhone("9876543210");
        updateDTO.setVillage("Pune");
        updateDTO.setState("Maharashtra");
        updateDTO.setCropType(CropType.RICE);
        updateDTO.setLandAcres(new BigDecimal("20.00"));

        Farmer updatedFarmer = new Farmer();
        updatedFarmer.setId(1L);
        updatedFarmer.setName("Ramesh Updated");
        updatedFarmer.setPhone("9876543210");
        updatedFarmer.setVillage("Pune");
        updatedFarmer.setState("Maharashtra");
        updatedFarmer.setCropType(CropType.RICE);
        updatedFarmer.setLandAcres(new BigDecimal("20.00"));

        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));
        when(farmerRepository.save(any(Farmer.class))).thenReturn(updatedFarmer);

        FarmerResponseDTO result = farmerService.updateFarmer(1L, updateDTO);

        assertThat(result.getName()).isEqualTo("Ramesh Updated");
        assertThat(result.getCropType()).isEqualTo(CropType.RICE);
        verify(farmerRepository, times(1)).save(any(Farmer.class));
    }

    @Test
    void updateFarmer_NotFound_ThrowsException() {
        when(farmerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> farmerService.updateFarmer(99L, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(farmerRepository, never()).save(any(Farmer.class));
    }

    @Test
    void deleteFarmer_Success() {
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));
        doNothing().when(farmerRepository).delete(farmer);

        assertThatNoException().isThrownBy(() -> farmerService.deleteFarmer(1L));

        verify(farmerRepository, times(1)).delete(farmer);
    }

    @Test
    void deleteFarmer_NotFound_ThrowsException() {
        when(farmerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> farmerService.deleteFarmer(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(farmerRepository, never()).delete(any(Farmer.class));
    }
}
