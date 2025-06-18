package com.backend.dashboard_tool.service;

import java.util.*;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.DTO.ProcessDTO;

/**
 * Service class for handling process-related operations.
 * This class provides methods to retrieve processes based on various criteria.
 */
@Service
public class ProcessService {
    private final ProcessRepository processRepository;

    /**
     * Constructor for ProcessService.
     * 
     * @param processRepository the ProcessRepository instance
     */
    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    /**
     * Retrieves sub-processes based on the provided parent ID, level, and type.
     * 
     * @param parentId the ID of the parent process (optional)
     * @param level the level of the process
     * @param type the type of the process (optional)
     * @return a list of ProcessEntity objects matching the criteria
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "processes", key = "#parentId + '-' + '-' + #type")
    public List<ProcessEntity> getSubProcesses(Long parentId, String type) {
        //If parent id is not null, then we must look for subprocesses from the parent.
        if(parentId != null){
            return processRepository.findSubprocessByParentId(parentId);
        }

        //If type is null, we cannot return processes at level 0.
        if(type == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
            "Type must be specified when parent id is not provided.");
        }
        
        //Return all processes at level 0 from type
        return processRepository.findByTypeLevel0(type);
    }

    /**
     * Creates a new process based on the provided JSON string.
     * 
     * @param json the JSON string containing process data
     * @return the created ProcessEntity object
     */
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "processes", key = "#processDTO.parent() + '-' + '-' + #processDTO.type"),
        @CacheEvict(value = "process", key = "#result.id")
    })
    public ProcessEntity createProcess(ProcessDTO processDTO) {
        //Convert ProcessDTO to ProcessEntity
        ProcessEntity processEntity = new ProcessEntity();
        processEntity.setName(processDTO.name());
        processEntity.setType(processDTO.type());
        processEntity.setDescription(processDTO.description());
        processEntity.setSoort(processDTO.soort());
        processEntity.setInternal(processDTO.internal());

        //If the process has a parent process, validate it
        if (processDTO.parent() != null) {
            // Validate parent process: it must exist and match the level and type
            ProcessEntity parent = processRepository.findById(processDTO.parent())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent process not found."));
            
            processEntity.setLevel(parent.getLevel() + 1);

            if (!parent.getType().equals(processEntity.getType())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent process type must match the new process type.");
            }
            processEntity.setParentProcess(parent);
        } else {
            processEntity.setLevel(0);
        }
        return processRepository.save(processEntity);
    }

    /**
     * Retrieves a process by its ID.
     * 
     * @param id the ID of the process
     * @return the ProcessEntity object with the specified ID
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "process", key = "#id")
    public ProcessEntity getProcessById(Long id) {
        return processRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Process not found."));
    }
}
