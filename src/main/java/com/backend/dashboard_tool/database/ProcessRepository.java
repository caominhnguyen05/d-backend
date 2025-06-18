package com.backend.dashboard_tool.database;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for accessing Process data.
 * This interface extends JpaRepository to provide CRUD operations for the ProcessEntity.
 */
public interface ProcessRepository extends JpaRepository<ProcessEntity, Long> {
    /**
     * Finds a ProcessEntity by its ID.
     *
     * @param id the ID of the ProcessEntity to find
     * @return an Optional containing the found ProcessEntity, or empty if not found
     */
    @NonNull
    Optional<ProcessEntity> findById(@NonNull Long id);

    /**
     * Finds all ProcessEntity instances with the specified type.
     *
     * @param type the type of the ProcessEntity to find
     * @return a list of ProcessEntity instances with the specified type
     */
    @NonNull
    List<ProcessEntity> findByType(@NonNull String type);

    /**
     * Finds all ProcessEntity instances with the specified type at level 0.
     * This method is used to retrieve top-level processes of a specific type.
     * @param type
     * @return a list of ProcessEntity instances with the specified type at level 0
     */

    @Query("SELECT p FROM process p WHERE p.type = :type AND p.level = 0")
    List<ProcessEntity> findByTypeLevel0(String type);

    /**
     * Find subprocesses of a given parent process.
     * @param parentId the ID of the parent process
     * @return a list of subprocesses belonging to the parent process
     */
    @Query("SELECT p FROM process p WHERE p.parentProcess.id = :parentId")
    List<ProcessEntity> findSubprocessByParentId(Long parentId);
}
