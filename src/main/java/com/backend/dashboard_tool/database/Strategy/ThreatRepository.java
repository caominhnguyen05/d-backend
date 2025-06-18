package com.backend.dashboard_tool.database.Strategy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.dashboard_tool.entity.Strategy.Threat;
/**
 * Repository interface for accessing Threat data.
 * This interface extends JpaRepository to provide CRUD operations for the Threat entity.
 */
public interface ThreatRepository extends JpaRepository<Threat, Long> {

}