package com.backend.dashboard_tool.database.Finance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Finance.Revenue;

/**
 * Repository interface for accessing Revenue data.
 * This interface extends JpaRepository to provide CRUD operations for the Revenue entity.
 */
public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    /**
     * Retrieves all Revenue entities associated with a specific process ID.
     *
     * This JPQL query joins the Revenue entity with its related Process entities
     * and filters the results by the given process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of Revenue entities linked to the specified process.
     */
    @Query("SELECT r FROM Revenue r JOIN r.processes p WHERE p.id = :processId")
    List<Revenue> findByProcessId(@Param("processId") Long processId);
}