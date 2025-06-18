package com.backend.dashboard_tool.database.People;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.People.Consultation;

/**
 * Repository interface for accessing Consultation data.
 * This interface extends JpaRepository to provide CRUD operations for the Consultation entity.
 */
public interface ConsultationRepository extends JpaRepository<Consultation, Long>{
    /**
     * Retrieves all Consultation entities associated with a specific process ID.
     *
     * This JPQL query joins the Application entity with its related Process entities
     * and filters the results by the given process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of Consultation entities linked to the specified process.
     */
    @Query("SELECT c FROM Consultation c JOIN c.processes p WHERE p.id = :processId")
    List<Consultation> findByProcessId(@Param("processId") Long processId);
}
