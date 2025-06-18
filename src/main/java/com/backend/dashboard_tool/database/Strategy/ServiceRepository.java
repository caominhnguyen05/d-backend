package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.Service;
/**
 * Repository interface for accessing Service data.
 * This interface extends JpaRepository to provide CRUD operations for the Service entity.
 */
public interface ServiceRepository extends JpaRepository<Service, Long> {
    //Query to find Service entities associated with a specific process ID
    @Query("SELECT s FROM Service s JOIN s.processes p WHERE p.id = :processId")
    List<Service> findByProcessId(@Param("processId") Long processId);
}
