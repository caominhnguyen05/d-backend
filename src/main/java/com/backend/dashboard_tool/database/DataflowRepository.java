package com.backend.dashboard_tool.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Process_Data.*;

/**
 * Repository interface for accessing Dataflow data.
 * This interface extends JpaRepository to provide CRUD operations for the Dataflow entity.
 */
public interface DataflowRepository extends JpaRepository<Dataflow, Long> {
    /**
     * Finds all Dataflow entities associated with a specific target process ID.
     *
     * @param processId the ID of the target process
     * @return a list of Dataflow entities associated with the specified target process ID
     */
    @Query("SELECT d FROM Dataflow d WHERE d.targetProcess.id = ?1")
    List<Dataflow> findByTargetProcessId(Long processId);

    /**
     * Finds all Dataflow entities associated with a specific source process ID.
     *
     * @param processId the ID of the source process
     * @return a list of Dataflow entities associated with the specified source process ID
     */
    @Query("SELECT d FROM Dataflow d WHERE d.sourceProcess.id = ?1")
    List<Dataflow> findBySourceProcessId(Long processId);

    /**
     * Finds all Dataflow entities associated with a specific source process ID.
     *
     * @param processId the ID of the source process
     * @return a list of Dataflow entities associated with the specified source process ID
     */
    @Query("""
    SELECT d FROM Dataflow d
    JOIN d.sourceProcess source
    JOIN d.targetProcess target
    WHERE source.id IN :ids AND target.id IN :ids
    """)
    List<Dataflow> findByProcessIds(@Param("ids") List<Long> ids);
    /**
     * Finds all input Dataflow entities for a specific process ID and parent ID.
     *
     * @param processId the ID of the process
     * @param parentId the ID of the parent process
     * @param type the type of the process
     * @return a list of Dataflow entities that are inputs to the specified process
     */
    @Query(
        """
        SELECT d FROM Dataflow d
        JOIN d.sourceProcess source
        WHERE d.targetProcess.id = :processId
        AND ((:parentId IS NULL AND (source.type <> :type OR source.parentProcess.id IS NOT NULL)) OR (:parentId IS NOT NULL AND (source.parentProcess.id IS NULL OR source.parentProcess.id <> :parentId)))
        """)
    List<Dataflow> findInputDataflows(@Param("processId") Long processId, @Param("parentId") Long parentId, @Param("type") String type);

    /**
     * Finds all output Dataflow entities for a specific process ID and parent ID.
     *
     * @param processId the ID of the process
     * @param parentId the ID of the parent process 
     * @return a list of Dataflow entities that are outputs to the specified process
     */
    @Query(
        """
        SELECT d FROM Dataflow d
        JOIN d.targetProcess target
        WHERE d.sourceProcess.id = :processId
        AND ((:parentId IS NULL AND (target.type <> :type OR target.parentProcess.id IS NOT NULL)) OR (:parentId IS NOT NULL AND (target.parentProcess.id IS NULL OR target.parentProcess.id <> :parentId)))
        """)
    List<Dataflow> findOutputDataflows(@Param("processId") Long processId, @Param("parentId") Long parentId, @Param("type") String type);
}
