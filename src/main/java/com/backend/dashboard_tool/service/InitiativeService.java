package com.backend.dashboard_tool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.database.Strategy.ProjectRepository;
import com.backend.dashboard_tool.database.Strategy.ImprovementRepository;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

@Service
public class InitiativeService {
    private final ImprovementRepository improvementRepository;
    private final ProjectRepository projectRepository;
    private final ProcessService processService;

    /**
     * Constructor for InitiativeService.
     *
     * @param improvementRepository the ImprovementRepository instance
     * @param projectRepository the ProjectRepository instance
     * @param processService the ProcessService instance
     */
    public InitiativeService(ImprovementRepository improvementRepository, ProjectRepository projectRepository, ProcessService processService) {
        this.improvementRepository = improvementRepository;
        this.projectRepository = projectRepository;
        this.processService = processService;
    }

    /**
     * Retrieves all initiative-related entities of a specific type.
     * This method fetches all records for the specified initiative type.
     * @param type
     * @return A list of entities for the specified type, or throws an exception if the type is unknown.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "initiatives", key = "#type")
    public List<?> getAllInitiativesByType(String type) {
        return switch (type.toLowerCase()) {
            case "improvement" -> improvementRepository.findAll();
            case "project" -> projectRepository.findAll();
            // add other initiative-related types here if needed
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown initiative type: " + type);
        };
    }

    /**
     * Retrieves a specific initiative entity by type and process ID.
     * This method fetches a single entity corresponding to the specified initiative type and process ID.
     *
     * @param type The type of initiative to retrieve (e.g., "improvement", "project").
     * @param processId The ID of the process associated with the initiative entity.
     * @return A list of matching entities for the specified initiative type and process ID,
     *         or throws an exception if no such entities exist.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "initiativesByProcess", key = "#type + '-' + #processId")
    public List<?> getInitiativesByTypeAndProcessId(String type, Long processId) {
        ProcessEntity process = processService.getProcessById(processId);
        if (process == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process with ID " + processId + " not found.");
        }

        return switch (type.toLowerCase()) {
            case "improvement" -> improvementRepository.findByProcessId(processId);
            case "project" -> projectRepository.findByProcessId(processId);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown initiative type: " + type);
        };
    }
}
