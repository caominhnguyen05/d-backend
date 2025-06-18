package com.backend.dashboard_tool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.database.Assets.AssetRepository;
import com.backend.dashboard_tool.database.Assets.FacilityRepository;
import com.backend.dashboard_tool.database.Assets.LocationRepository;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

@Service
public class InfrastructureService {
    private final LocationRepository locationRepository;
    private final AssetRepository assetRepository;
    private final FacilityRepository facilityRepository;
    private final ProcessService processService;

    /**
     * Constructor for InfrastructureService.
     *
     * @param dataStoreRepository the DataStoreRepository instance
     * @param applicationRepository the ApplicationRepository instance
     * @param systemEntityRepository the SystemEntityRepository instance
     * @param processService the ProcessService instance
     */
    public InfrastructureService(LocationRepository locationRepository, AssetRepository assetRepository, FacilityRepository facilityRepository, ProcessService processService) {
        this.locationRepository = locationRepository;
        this.assetRepository = assetRepository;
        this.facilityRepository = facilityRepository;
        this.processService = processService;
    }

    /**
     * Retrieves all ict-related entities of a specific type.
     * This method fetches all records for the specified ict type.
     * @param type
     * @return A list of entities for the specified type, or throws an exception if the type is unknown.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "infrastructures", key = "#type")
    public List<?> getAllInfrastructuresByType(String type) {
        return switch (type.toLowerCase()) {
            case "asset" -> assetRepository.findAll();
            case "facility" -> facilityRepository.findAll();
            case "location" -> locationRepository.findAll();
            // add other asset-related types here if needed
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown infrastructure type: " + type);
        };
    }

    /**
     * Retrieves a specific infrastructure entity by type and process ID.
     * This method fetches a single entity corresponding to the specified infrastructure type and process ID.
     *
     * @param type The type of infrastructure to retrieve (e.g., "datastore", "application", "system").
     * @param processId The ID of the process associated with the infrastructure entity.
     * @return A list of matching entities for the specified infrastructure type and process ID,
     *         or throws an exception if no such entities exist.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "infrastructureByProcess", key = "#type + '-' + #processId")
    public List<?> getInfrastructuresByTypeAndProcessId(String type, Long processId) {
        ProcessEntity process = processService.getProcessById(processId);
        if (process == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process with ID " + processId + " not found.");
        }

        return switch (type.toLowerCase()) {
            case "asset" -> assetRepository.findByProcessId(processId);
            case "facility" -> facilityRepository.findByProcessId(processId);
            case "location" -> locationRepository.findByProcessId(processId);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown infrastructure type: " + type);
        };
    }
}
