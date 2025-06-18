package com.backend.dashboard_tool.database.Process_Data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Process_Data.DataStore;

public interface DataStoreRepository extends JpaRepository<DataStore, Long> {
    // Query to find all DataStore entities associated with a specific process ID
    @Query("SELECT ds FROM DataStore ds JOIN ds.processes p WHERE p.id = :processId")
    List<DataStore> findByProcessId(@Param("processId") Long processId);    
}
