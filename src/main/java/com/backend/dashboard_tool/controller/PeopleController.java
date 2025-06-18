package com.backend.dashboard_tool.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dashboard_tool.service.PeopleService;

/**
 * Controller for handling people-related requests.
 * This class provides endpoints for retrieving departments, employees, and organizations.
 */
@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;

    /**
     * Constructor for PeopleController.
     *
     * @param peopleService the PeopleService instance
     */
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    /**
     * Retrieves all people-related entities of a specific type.
     * This endpoint fetches all records for the specified people type.
     *
     * @param type The type of people entity to retrieve (e.g., "department", "employee", "function", "organization", "role").
     * @return A ResponseEntity containing the list of entities for the specified type,
     *         or a bad request response if the type is unknown.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllPeopleByType(@RequestParam String type) {
        List<?> items = peopleService.getAllPeopleByType(type.toLowerCase());
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }

    /**
     * Retrieves a specific people entity by type and process ID.
     * This endpoint fetches a single entity corresponding to the specified people type and process ID.
     *
     * @param type The type of people entity to retrieve (e.g., "department", "employee", "function", "organization", "role").
     * @param processId The ID of the process associated with the people entity.
     * @return A ResponseEntity containing the list of matching entities for the specified people type and process ID,
     *         or a not found response if no such entities exist.
     */
    @GetMapping("/process")
    public ResponseEntity<?> getPeopleByTypeAndProcessId(@RequestParam String type, @RequestParam Long processId) {
        List<?> items = peopleService.getPeopleByTypeAndProcessId(type.toLowerCase(), processId);
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }
}
