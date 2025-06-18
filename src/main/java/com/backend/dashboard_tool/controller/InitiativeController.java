package com.backend.dashboard_tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.backend.dashboard_tool.service.InitiativeService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/initiative")
public class InitiativeController {
    @Autowired
    private InitiativeService initiativeService;

    /**
     * Retrieves all layers of a specific type.
     * This endpoint fetches all entities corresponding to the specified layer type.
     *
     * @param type The type of layer to retrieve ( "improvement", "project").
     * @return A ResponseEntity containing the list of entities for the specified layer type,
     *         or a bad request response if the type is unknown.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllLayersByType(@RequestParam String type) {
        List<?> items = initiativeService.getAllInitiativesByType(type.toLowerCase());
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }
    /**
     * Retrieves a specific layer entity by type and process ID.
     * This endpoint fetches a single entity corresponding to the specified layer type and process ID.
     *
     * @param type The type of layer to retrieve ("improvement", "project").
     * @param processId The ID of the process associated with the layer entity.
     * @return A ResponseEntity containing the entity for the specified layer type and process ID,
     *         or a not found response if the entity does not exist.
     */
    @GetMapping("/process")
    public ResponseEntity<?> getLayerByTypeAndProcessId(@RequestParam String type, @RequestParam Long processId) {
        List<?> items = initiativeService.getInitiativesByTypeAndProcessId(type.toLowerCase(), processId);
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }
}
