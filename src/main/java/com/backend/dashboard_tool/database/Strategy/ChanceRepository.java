package com.backend.dashboard_tool.database.Strategy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.dashboard_tool.entity.Strategy.Chance;
/**
 * Repository interface for accessing Chance data.
 * This interface extends JpaRepository to provide CRUD operations for the Chance entity.
 */
public interface ChanceRepository extends JpaRepository<Chance, Long> {
}
