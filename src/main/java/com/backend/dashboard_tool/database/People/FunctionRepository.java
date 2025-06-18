package com.backend.dashboard_tool.database.People;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.People.Function;

/**
 * Repository interface for accessing Function data.
 * This interface extends JpaRepository to provide CRUD operations for the Function entity.
 */
public interface FunctionRepository extends JpaRepository<Function, Long> {
    /**
     * Retrieves all Function entities associated with a specific process ID.
     *
     * This JPQL query joins the Function entity with its related Process entities
     * and filters the results by the given process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of Function entities linked to the specified process.
     */
    @Query("SELECT f FROM Function f JOIN f.processes p WHERE p.id = :processId")
    List<Function> findByProcessId(@Param("processId") Long processId);
}