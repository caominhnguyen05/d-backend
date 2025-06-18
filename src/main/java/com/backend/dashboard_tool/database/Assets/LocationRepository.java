package com.backend.dashboard_tool.database.Assets;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.backend.dashboard_tool.entity.Assets_Facilities.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long>{
    /**
     * Retrieves all Location entities associated with a specific process ID.
     *
     * This query fetches all Location records that are joined with a Process
     * having the specified ID.
     *
     * @param processId The ID of the process linked to the location.
     * @return A list of Location entities associated with the given process.
     */
    @Query("SELECT l FROM Location l JOIN l.processes p WHERE p.id = :processId")
    List<Location> findByProcessId(@Param("processId") Long processId);
}
