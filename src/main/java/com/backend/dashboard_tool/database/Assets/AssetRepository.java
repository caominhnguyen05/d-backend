package com.backend.dashboard_tool.database.Assets;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.dashboard_tool.entity.Assets_Facilities.Asset;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for accessing Asset data.
 * This interface extends JpaRepository to provide CRUD operations for the Asset entity.
 */
public interface AssetRepository extends JpaRepository<Asset, Long>{
    /**
     * Retrieves all Asset entities associated with a specific process ID.
     *
     * Joins the Asset entity with its related Process entities and returns all
     * assets where the linked process ID matches the provided value.
     *
     * @param processId The ID of the process to filter by.
     * @return A list of Asset entities associated with the given process.
     */
    @Query("SELECT a FROM Asset a JOIN a.processes p WHERE p.id = :processId")
    List<Asset> findByProcessId(@Param("processId") Long processId);

}
