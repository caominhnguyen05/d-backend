package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.Product;
/**
 * Repository interface for accessing Product data.
 * This interface extends JpaRepository to provide CRUD operations for the Product entity.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    //Query to find all Product entities associated with a specific process ID
    @Query("SELECT pr FROM Product pr JOIN pr.processes p WHERE p.id = :processId")
    List<Product> findByProcessId(@Param("processId") Long processId);
}
