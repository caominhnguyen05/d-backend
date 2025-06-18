package com.backend.dashboard_tool.database.Assets;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.backend.dashboard_tool.entity.Assets_Facilities.Application;

/**
 * Repository interface for accessing Application data.
 * This interface extends JpaRepository to provide CRUD operations for the Application entity.
 */
public interface ApplicationRepository extends JpaRepository<Application, Long>{
    /**
     * Retrieves all Application entities associated with a specific process ID.
     *
     * This JPQL query joins the Application entity with its related Process entities
     * and filters the results by the given process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of Application entities linked to the specified process.
     */
    @Query("SELECT a FROM Application a JOIN a.processes p WHERE p.id = :processId")
    List<Application> findByProcessId(@Param("processId") Long processId);

}
