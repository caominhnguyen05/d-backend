package com.backend.dashboard_tool.database.People;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.dashboard_tool.entity.People.Employee;

/**
 * Repository interface for accessing Employee data.
 * This interface extends JpaRepository to provide CRUD operations for the Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Find employees by department ID
    List<Employee> findByDepartmentId(Long departmentId);

    /**
     * Retrieves all Employee entities associated with a specific process ID.
     *
     * This JPQL query joins the Employee entity with its related Process entities
     * and filters the results by the given process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of Employee entities linked to the specified process.
     */
    @Query("SELECT e FROM Employee e JOIN e.processes p WHERE p.id = :processId")
    List<Employee> findByProcessId(@Param("processId") Long processId);
}
