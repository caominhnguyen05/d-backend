package com.backend.dashboard_tool.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Process_Data.Result;

public interface ResultRepository extends JpaRepository<Result, Long>{
    /**
     * Finds all Result entities associated with a specific source process ID.
     *
     * @param id the ID of the source process
     * @return a list of Result entities associated with the specified target process ID
     */
    @Query("SELECT r FROM Result r WHERE r.sourceProcess.id = :id")
    List<Result> findBySourceProcessId(@Param("id") Long id);
}
