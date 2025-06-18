package com.backend.dashboard_tool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.Strategy.AuditRepository;
import com.backend.dashboard_tool.database.Strategy.LegislationRepository;
import com.backend.dashboard_tool.database.DocumentRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

@Service
public class ControlService {
    private final AuditRepository auditRepository;
    private final LegislationRepository legislationRepository;
    private final DocumentRepository documentRepository;
    private final ProcessService processService;

    /**
     * Constructor for ControlService.
     *
     * @param auditRepository the AuditRepository instance
     * @param legislationRepository the LegislationRepository instance
     * @param documentRepository the DocumentRepository instance
     * @param processService the ProcessService instance
     */
    public ControlService(AuditRepository auditRepository, LegislationRepository legislationRepository, DocumentRepository documentRepository, ProcessService processService) {
        this.auditRepository = auditRepository;
        this.legislationRepository = legislationRepository;
        this.documentRepository = documentRepository;
        this.processService = processService;
    }

    /**
     * Retrieves all control-related entities of a specific type.
     * This method fetches all records for the specified control type.
     * @param type
     * @return A list of entities for the specified type, or throws an exception if the type is unknown.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "controls", key = "#type")
    public List<?> getAllControlsByType(String type) {
        return switch (type.toLowerCase()) {
            case "audit" -> auditRepository.findAll();
            case "legislation" -> legislationRepository.findAll();
            case "document" -> documentRepository.findAll();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown control type: " + type);
        };
    }

    /**
     * Retrieves a specific control entity by type and process ID.
     * This method fetches a single entity corresponding to the specified control type and process ID.
     *
     * @param type The type of control to retrieve (e.g., "audit", "legislation", "document").
     * @param processId The ID of the process associated with the control entity.
     * @return A list of matching entities for the specified control type and process ID,
     *         or throws an exception if no such entities exist.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "controlByProcess", key = "#type + '-' + #processId")
    public List<?> getControlsByTypeAndProcessId(String type, Long processId) {
        ProcessEntity process = processService.getProcessById(processId);
        if (process == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process with ID " + processId + " not found.");
        }

        return switch (type.toLowerCase()) {
            case "audit" -> auditRepository.findByProcessId(processId);
            case "legislation" -> legislationRepository.findByProcessId(processId);
            case "document" -> documentRepository.findByProcessId(processId);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown control type: " + type);
        };
    }
}
