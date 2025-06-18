package com.backend.dashboard_tool.database.Assets;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.dashboard_tool.entity.Assets_Facilities.Facility;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for accessing Facility data.
 * This interface extends JpaRepository to provide CRUD operations for the Facility entity.
 */
public interface FacilityRepository extends JpaRepository<Facility, Long>{
    // Query to find all Facility entities associated with a specific processID
    @Query("SELECT f FROM Facility f JOIN f.processes p WHERE p.id = :processId")
    List<Facility> findByProcessId(@Param("processId") Long processId);
}
