package com.backend.dashboard_tool.database.Assets;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Assets_Facilities.SystemEntity;

public interface SystemEntityRepository extends JpaRepository<SystemEntity, Long> {
    /**
     * Retrieves all SystemEntity records associated with a specific process ID.
     *
     * This JPQL query joins SystemEntity with its associated Process entities
     * and filters by the provided process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of SystemEntity instances linked to the given process ID.
     */
    @Query("SELECT s FROM system s JOIN s.processes p WHERE p.id = :processId")
    List<SystemEntity> findByProcessId(@Param("processId") Long processId);
}
