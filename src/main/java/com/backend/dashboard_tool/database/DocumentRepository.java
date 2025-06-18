package com.backend.dashboard_tool.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Document;

/**
 * Repository interface for accessing Document data.
 * This interface extends JpaRepository to provide CRUD operations for the Document entity.
 */
public interface DocumentRepository extends JpaRepository<Document, Long>{
    /**
     * Retrieves all Document entities associated with a specific process ID.
     *
     * This JPQL query joins the Document entity with its related Process entities
     * and filters the results by the given process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of Document entities linked to the specified process.
     */
    @Query("SELECT d FROM Document d JOIN d.processes p WHERE p.id = :processId")
    List<Document> findByProcessId(@Param("processId") Long processId);
}
