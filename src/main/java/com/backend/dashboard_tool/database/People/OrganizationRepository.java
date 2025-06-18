package com.backend.dashboard_tool.database.People;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.People.Organization;

/**
 * Repository interface for accessing Organization data.
 * This interface extends JpaRepository to provide CRUD operations for the Organization entity.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long>{
    /**
     * Retrieves all Organization entities associated with a specific process ID.
     *
     * This JPQL query joins the Organization entity with its related Process entities
     * and filters the results by the given process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of Organization entities linked to the specified process.
     */
    @Query("SELECT o FROM Organization o JOIN o.processes p WHERE p.id = :processId")
    List<Organization> findByProcessId(@Param("processId") Long processId);
}
