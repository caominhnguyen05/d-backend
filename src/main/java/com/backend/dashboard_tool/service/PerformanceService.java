package com.backend.dashboard_tool.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.Strategy.KPIRepository;
import com.backend.dashboard_tool.database.Strategy.MitigationMeasureRepository;
import com.backend.dashboard_tool.database.Strategy.RiskRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerformanceService {
    private final KPIRepository kpiRepository;
    private final RiskRepository riskRepository;
    private final MitigationMeasureRepository mitigationMeasureRepository;
    private final ProcessService processService;

    /**
     * Constructor for PerformanceService.
     *
     * @param kpiRepository the KPIRepository instance
     * @param riskRepository the RiskRepository instance
     * @param mitigationMeasureRepository the MitigationMeasureRepository instance
     * @param processService the ProcessService instance
     */
    public PerformanceService(KPIRepository kpiRepository, RiskRepository riskRepository,
                              MitigationMeasureRepository mitigationMeasureRepository, ProcessService processService) {
        this.kpiRepository = kpiRepository;
        this.riskRepository = riskRepository;
        this.mitigationMeasureRepository = mitigationMeasureRepository;
        this.processService = processService;
    }

   /**
     * Retrieves all performance-related entities of a specific type.
     * This method fetches all records for the specified performance type.
     * @param type
     * @return A list of entities for the specified type, or throws an exception if the type is unknown.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "performances", key = "#type")
    public List<?> getAllPerformancesByType(String type) {
        return switch (type.toLowerCase()) {
            case "kpi" -> kpiRepository.findAll();
            case "risk" -> riskRepository.findAll();
            case "mitigation-measure" -> mitigationMeasureRepository.findAll();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown performance type: " + type);
        };
    }

    /**
     * Retrieves a specific performance entity by type and process ID.
     * This method fetches a single entity corresponding to the specified performance type and process ID.
     *
     * @param type The type of performance to retrieve (e.g., "kpi", "risk", "mitigation-measure").
     * @param processId The ID of the process associated with the performance entity.
     * @return A list of matching entities for the specified performance type and process ID,
     *         or throws an exception if no such entities exist.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "performancesByProcess", key = "#type + '-' + #processId")
    public List<?> getPerformancesByTypeAndProcessId(String type, Long processId) {
        ProcessEntity process = processService.getProcessById(processId);
        if (process == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process with ID " + processId + " not found.");
        }

        return switch (type.toLowerCase()) {
            case "kpi" -> kpiRepository.findByProcessId(processId);
            case "risk" -> riskRepository.findByProcessId(processId);
            case "mitigation-measure" -> mitigationMeasureRepository.findByProcessId(processId);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown performance type: " + type);
        };
    }
}
