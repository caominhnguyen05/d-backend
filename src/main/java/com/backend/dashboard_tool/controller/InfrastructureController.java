package com.backend.dashboard_tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.backend.dashboard_tool.service.InfrastructureService;

/**
 * Controller for handling infrastructure-related requests.
 * This class provides endpoints for retrieving assets, facilities, and locations.
 */
@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/infrastructure")
public class InfrastructureController {
    @Autowired
    private InfrastructureService infrastructureService;

    /**
     * Retrieves all asset-related entities of a specific type.
     * This endpoint fetches all records for the specified asset type.
     *
     * @param type The type of asset entity to retrieve (e.g., "asset", "facility", "location").
     * @return A ResponseEntity containing the list of entities for the specified type,
     *         or a bad request response if the type is unknown.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllAssetsByType(@RequestParam String type) {
        List<?> items = infrastructureService.getAllInfrastructuresByType(type.toLowerCase());
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }

    /**
     * Retrieves a specific asset entity by type and process ID.
     * This endpoint fetches a single entity corresponding to the specified asset type and process ID.
     *
     * @param type The type of asset to retrieve (e.g., "asset", "facility", "location").
     * @param processId The ID of the process associated with the asset entity.
     * @return A ResponseEntity containing the list of matching entities for the specified asset type and process ID,
     *         or a not found response if no such entities exist.
     */
    @GetMapping("/process")
    public ResponseEntity<?> getAssetByTypeAndProcessId(@RequestParam String type, @RequestParam Long processId) {
        List<?> items = infrastructureService.getInfrastructuresByTypeAndProcessId(type.toLowerCase(), processId);
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }
}
