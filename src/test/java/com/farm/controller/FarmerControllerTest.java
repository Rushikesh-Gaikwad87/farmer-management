package com.farm.controller;

import com.farm.dto.FarmerRequestDTO;
import com.farm.dto.FarmerResponseDTO;
import com.farm.entity.CropType;
import com.farm.exception.GlobalExceptionHandler;
import com.farm.exception.ResourceNotFoundException;
import com.farm.service.FarmerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FarmerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FarmerService farmerService;

    @InjectMocks
    private FarmerController farmerController;

    private ObjectMapper objectMapper;
    private FarmerResponseDTO responseDTO;
    private FarmerRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(farmerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        responseDTO = new FarmerResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Ramesh Kumar");
        responseDTO.setPhone("9876543210");
        responseDTO.setVillage("Nashik");
        responseDTO.setState("Maharashtra");
        responseDTO.setCropType(CropType.WHEAT);
        responseDTO.setLandAcres(new BigDecimal("12.50"));

        requestDTO = new FarmerRequestDTO();
        requestDTO.setName("Ramesh Kumar");
        requestDTO.setPhone("9876543210");
        requestDTO.setVillage("Nashik");
        requestDTO.setState("Maharashtra");
        requestDTO.setCropType(CropType.WHEAT);
        requestDTO.setLandAcres(new BigDecimal("12.50"));
    }

    @Test
    void getAllFarmers_Returns200() throws Exception {
        when(farmerService.getAllFarmers()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/farmers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ramesh Kumar"))
                .andExpect(jsonPath("$[0].phone").value("9876543210"));

        verify(farmerService, times(1)).getAllFarmers();
    }

    @Test
    void getFarmerById_Found_Returns200() throws Exception {
        when(farmerService.getFarmerById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/farmers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ramesh Kumar"))
                .andExpect(jsonPath("$.state").value("Maharashtra"));
    }

    @Test
    void getFarmerById_NotFound_Returns404() throws Exception {
        when(farmerService.getFarmerById(99L))
                .thenThrow(new ResourceNotFoundException("Farmer not found with id: 99"));

        mockMvc.perform(get("/api/farmers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Farmer not found with id: 99"));
    }

    @Test
    void createFarmer_ValidRequest_Returns201() throws Exception {
        when(farmerService.createFarmer(any(FarmerRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/farmers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ramesh Kumar"));

        verify(farmerService, times(1)).createFarmer(any(FarmerRequestDTO.class));
    }

    @Test
    void createFarmer_InvalidRequest_Returns400() throws Exception {
        FarmerRequestDTO invalidDTO = new FarmerRequestDTO();
        invalidDTO.setName("");
        invalidDTO.setPhone("");
        invalidDTO.setCropType(CropType.WHEAT);
        invalidDTO.setLandAcres(new BigDecimal("5.00"));

        mockMvc.perform(post("/api/farmers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(farmerService, never()).createFarmer(any(FarmerRequestDTO.class));
    }

    @Test
    void updateFarmer_Success_Returns200() throws Exception {
        when(farmerService.updateFarmer(eq(1L), any(FarmerRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/farmers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ramesh Kumar"));
    }

    @Test
    void updateFarmer_NotFound_Returns404() throws Exception {
        when(farmerService.updateFarmer(eq(99L), any(FarmerRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Farmer not found with id: 99"));

        mockMvc.perform(put("/api/farmers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFarmer_Success_Returns204() throws Exception {
        doNothing().when(farmerService).deleteFarmer(1L);

        mockMvc.perform(delete("/api/farmers/1"))
                .andExpect(status().isNoContent());

        verify(farmerService, times(1)).deleteFarmer(1L);
    }

    @Test
    void deleteFarmer_NotFound_Returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Farmer not found with id: 99"))
                .when(farmerService).deleteFarmer(99L);

        mockMvc.perform(delete("/api/farmers/99"))
                .andExpect(status().isNotFound());
    }
}