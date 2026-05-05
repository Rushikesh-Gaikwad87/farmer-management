package com.farm.repository;

import com.farm.entity.Farmer;
import com.farm.entity.CropType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    // Spring Data JPA generates the SQL for these automatically
    List<Farmer> findByState(String state);
    Page<Farmer> findByState(String state, Pageable pageable);

    List<Farmer> findByCropType(CropType cropType);

    Optional<Farmer> findByPhone(String phone);

    boolean existsByPhone(String phone);

    // Paginated findAll (inherited but explicitly declared for clarity)
    Page<Farmer> findAll(Pageable pageable);

    // Custom JPQL Queries
    // Search by name — case insensitive, partial match
    @Query("SELECT f FROM Farmer f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Farmer> searchByName(@Param("name") String name);

    // Find farmers with land between min and max acres
    @Query("SELECT f FROM Farmer f WHERE f.landAcres BETWEEN :minAcres AND :maxAcres ORDER BY f.landAcres ASC")
    List<Farmer> findByLandAcresBetween(
        @Param("minAcres") BigDecimal minAcres,
        @Param("maxAcres") BigDecimal maxAcres);

    // Find farmers by state AND crop type
    @Query("SELECT f FROM Farmer f WHERE f.state = :state AND f.cropType = :cropType")
    List<Farmer> findByStateAndCrop(
        @Param("state") String state,
        @Param("cropType") CropType cropType);

    // Count farmers per state — returns summary data
    @Query("SELECT f.state, COUNT(f) FROM Farmer f GROUP BY f.state ORDER BY COUNT(f) DESC")
    List<Object[]> countFarmersByState();

    // Get total land acres per state
    @Query("SELECT f.state, SUM(f.landAcres) FROM Farmer f GROUP BY f.state")
    List<Object[]> totalLandAcresByState();
}