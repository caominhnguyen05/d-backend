package com.backend.dashboard_tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dashboard_tool.service.ControlService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("control")
public class ControlController {
    @Autowired
    private ControlService controlService;

        /**
     * Retrieves all asset-related entities of a specific type.
     * This endpoint fetches all records for the specified asset type.
     *
     * @param type The type of asset entity to retrieve (e.g., "audit", "legislation").
     * @return A ResponseEntity containing the list of entities for the specified type,
     *         or a bad request response if the type is unknown.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllControlByType(@RequestParam String type) {
        List<?> items = controlService.getAllControlsByType(type.toLowerCase());
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }
    /**
     * Retrieves a specific control entity by type and process ID.
     * This endpoint fetches a single entity corresponding to the specified control type and process ID.
     *
     * @param type The type of control to retrieve (e.g., "audit", "legislation", "document").
     * @param processId The ID of the process associated with the control entity.
     * @return A ResponseEntity containing the list of matching entities for the specified control type and process ID,
     *         or a not found response if no such entities exist.
     */
    @GetMapping("/process")
    public ResponseEntity<?> getControlByTypeAndProcessId(@RequestParam String type, @RequestParam Long processId) {
        List<?> items = controlService.getControlsByTypeAndProcessId(type.toLowerCase(), processId);
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }
}
