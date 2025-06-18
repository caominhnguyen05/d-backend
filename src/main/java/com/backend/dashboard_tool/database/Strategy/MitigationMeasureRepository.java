package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.MitigationMeasure;
/**
 * Repository interface for accessing MitigationMeasure data.
 * This interface extends JpaRepository to provide CRUD operations for the MitigationMeasure entity.
 */
public interface MitigationMeasureRepository extends JpaRepository<MitigationMeasure, Long> {
    //Query to find all Product entities associated with a specific process ID
    @Query("SELECT m FROM MitigationMeasure m JOIN m.processes p WHERE p.id = :processId")
    List<MitigationMeasure> findByProcessId(@Param("processId") Long processId);
}
