package com.backend.dashboard_tool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.service.ProcessService;
import com.backend.dashboard_tool.DTO.ProcessDTO;

/**
 * Controller for handling process-related requests.
 * This class provides endpoints for retrieving all processes, a specific process by ID,
 * and sub-processes based on parent ID, level, and type.
 * Cross-origin requests are allowed from ${frontend.url}.
 */
@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/process")
public class ProcessController {
    /**
     * Process repository for accessing process data.
     */
    private final ProcessRepository processRepository;
    /**
     * Process service for accessing process data.
     */
    private final ProcessService processService;

    /**
     * Constructor for ProcessController.
     * 
     * @param processRepository the ProcessRepository instance
     * @param processService the ProcessService instance
     */
    public ProcessController(ProcessRepository processRepository, ProcessService processService) {
        this.processRepository = processRepository;
        this.processService = processService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProcessEntity> createProcess(@Validated @RequestBody ProcessDTO processDTO) {
        ProcessEntity processEntity = processService.createProcess(processDTO);
        if(processEntity == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(processEntity);
    }

    /**
     * Endpoint for retrieving all processes.
     * This method returns a list of all process records in the database.
     * 
     * @return an iterable of all ProcessEntity objects
     */
    @GetMapping("/all")
    public Iterable<ProcessEntity> getAllProcesses() {
        return processRepository.findAll();
    }

    /**
     * Endpoint for retrieving a specific process by ID.
     * This method returns the process record with the specified ID.
     * 
     * @param id the ID of the process to retrieve
     * @return the ProcessEntity object with the specified ID
     */
    @GetMapping("/{id}")
    public ProcessEntity getById(@PathVariable Long id) {
        return processRepository.findById(id).orElseThrow(() -> 
        new ResponseStatusException(HttpStatus.NOT_FOUND, "The following process id does not exist: " + id));
    }

    /**
     * Endpoint for retrieving sub-processes based on parent ID, and type.
     * This method returns a list of sub-processes that match the specified criteria.
     * 
     * @param parentId the ID of the parent process (optional)
     * @param type the type of the process (optional)
     * @return an iterable of ProcessEntity objects matching the criteria
     */
    @GetMapping("/subprocess")
    public Iterable<ProcessEntity> getSubProcesses(
        @RequestParam(required = false) Long parentId,
        @RequestParam(required = false) String type
    ){
        return processService.getSubProcesses(parentId, type);
    }
}

