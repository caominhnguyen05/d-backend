package com.backend.dashboard_tool.database.People;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.People.Role;

/**
 * Repository interface for accessing Role data.
 * This interface extends JpaRepository to provide CRUD operations for the Role entity.
 */
public interface RoleRepository extends JpaRepository<Role, Long>{
    /**
     * Retrieves all Role entities associated with a specific process ID.
     *
     * This JPQL query joins the Role entity with its related Process entities
     * and filters the results by the given process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of Role entities linked to the specified process.
     */
    @Query("SELECT r FROM Role r JOIN r.processes p WHERE p.id = :processId")
    List<Role> findByProcessId(@Param("processId") Long processId);
}
