package com.backend.dashboard_tool.database.Strategy;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Process_Data.Improvement;

public interface ImprovementRepository extends JpaRepository<Improvement, Long> {
    // Query to find all Improvement entities associated with a specific process ID
    @Query("SELECT im FROM Improvement im JOIN im.processes p WHERE p.id = :processId")
    List<Improvement> findByProcessId(@Param("processId") Long processId);    
}
