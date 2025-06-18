package com.backend.dashboard_tool.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.Finance.CostRepository;
import com.backend.dashboard_tool.database.Finance.RevenueRepository;
import com.backend.dashboard_tool.database.Strategy.GoalRepository;
import com.backend.dashboard_tool.database.Strategy.ProductRepository;
import com.backend.dashboard_tool.database.Strategy.ServiceRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;

@Service
public class RelevanceService {
    private final CostRepository costRepository;
    private final RevenueRepository revenueRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final GoalRepository goalRepository;
    private final ProcessService processService;

    /**
     * Constructor for RelevanceService.
     *
     * @param costRepository the CostRepository instance
     * @param revenueRepository the RevenueRepository instance
     * @param productRepository the ProductRepository instance
     * @param serviceRepository the ServiceRepository instance
     * @param goalRepository the GoalRepository instance
     * @param processService the ProcessService instance
     */
    public RelevanceService(CostRepository costRepository, RevenueRepository revenueRepository,
                            ProductRepository productRepository, ServiceRepository serviceRepository,
                            GoalRepository goalRepository, ProcessService processService) {
        this.costRepository = costRepository;
        this.revenueRepository = revenueRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.goalRepository = goalRepository;
        this.processService = processService;
    }

    /**
     * Retrieves all relevances of a specific type.
     * This endpoint fetches all entities corresponding to the specified relevance type.
     *
     * @param type The type of relevance to retrieve ("cost", "revenue", "product", "service", "goal").
     * @return A ResponseEntity containing the list of entities for the specified relevance type,
     *         or a bad request response if the type is unknown.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "relevances", key = "#type")
    public List<?> getAllRelevancesByType(@RequestParam String type) {
        return switch (type.toLowerCase()) {
            case "cost" -> costRepository.findAll();
            case "revenue" -> revenueRepository.findAll();
            case "product" -> productRepository.findAll();
            case "service" -> serviceRepository.findAll();
            case "goal" -> goalRepository.findAll();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown performance type: " + type);
        };
    }

    /**
     * Retrieves a specific relevances entity by type and process ID.
     * This endpoint fetches a single entity corresponding to the specified relevances type and process ID.
     *
     * @param type The type of relevance to retrieve ("cost", "revenue", "product", "service", "goal").
     * @param processId The ID of the process associated with the relevances entity.
     * @return A ResponseEntity containing the entity for the specified relevances type and process ID,
     *         or a not found response if the entity does not exist.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "relevancesByProcess", key = "#type + '-' + #processId")
    public List<?> getRelevanceByTypeAndProcessId(@RequestParam String type, @RequestParam Long processId) {
        ProcessEntity process = processService.getProcessById(processId);
        if (process == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process with ID " + processId + " not found.");
        }

        return switch (type.toLowerCase()) {
            case "cost" -> costRepository.findByProcessId(processId);
            case "revenue" -> revenueRepository.findByProcessId(processId);
            case "product" -> productRepository.findByProcessId(processId);
            case "service" -> serviceRepository.findByProcessId(processId);
            case "goal" -> goalRepository.findByProcessId(processId);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown relevance type: " + type);
        };
    }
}
