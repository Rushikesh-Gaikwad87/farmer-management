package com.farm.repository;

import com.farm.entity.Farmer;
import com.farm.entity.CropType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    // Spring Data JPA generates the SQL for these automatically
    List<Farmer> findByState(String state);

    List<Farmer> findByCropType(CropType cropType);

    Optional<Farmer> findByPhone(String phone);

    boolean existsByPhone(String phone);
}