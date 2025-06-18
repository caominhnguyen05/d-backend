package com.backend.dashboard_tool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.Process_Data.DataStoreRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.database.Assets.ApplicationRepository;
import com.backend.dashboard_tool.database.Assets.SystemEntityRepository;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

@Service
public class ICTService {
    private final DataStoreRepository dataStoreRepository;
    private final ApplicationRepository applicationRepository;
    private final SystemEntityRepository systemEntityRepository;
    private final ProcessService processService;

    /**
     * Constructor for ICTService.
     *
     * @param dataStoreRepository the DataStoreRepository instance
     * @param applicationRepository the ApplicationRepository instance
     * @param systemEntityRepository the SystemEntityRepository instance
     * @param processService the ProcessService instance
     */
    public ICTService(DataStoreRepository dataStoreRepository, ApplicationRepository applicationRepository, SystemEntityRepository systemEntityRepository, ProcessService processService) {
        this.dataStoreRepository = dataStoreRepository;
        this.applicationRepository = applicationRepository;
        this.systemEntityRepository = systemEntityRepository;
        this.processService = processService;
    }

    /**
     * Retrieves all ict-related entities of a specific type.
     * This method fetches all records for the specified ict type.
     * @param type
     * @return A list of entities for the specified type, or throws an exception if the type is unknown.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "icts", key = "#type")
    public List<?> getAllIctsByType(String type) {
        return switch (type.toLowerCase()) {
            case "datastore" -> dataStoreRepository.findAll();
            case "application" -> applicationRepository.findAll();
            case "system" -> systemEntityRepository.findAll();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown ict type: " + type);
        };
    }

    /**
     * Retrieves a specific ict entity by type and process ID.
     * This method fetches a single entity corresponding to the specified ict type and process ID.
     *
     * @param type The type of ict to retrieve (e.g., "datastore", "application", "system").
     * @param processId The ID of the process associated with the ict entity.
     * @return A list of matching entities for the specified ict type and process ID,
     *         or throws an exception if no such entities exist.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "ictByProcess", key = "#type + '-' + #processId")
    public List<?> getIctsByTypeAndProcessId(String type, Long processId) {
        ProcessEntity process = processService.getProcessById(processId);
        if (process == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process with ID " + processId + " not found.");
        }

        return switch (type.toLowerCase()) {
            case "datastore" -> dataStoreRepository.findByProcessId(processId);
            case "application" -> applicationRepository.findByProcessId(processId);
            case "system" -> systemEntityRepository.findByProcessId(processId);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown ict type: " + type);
        };
    }
}
