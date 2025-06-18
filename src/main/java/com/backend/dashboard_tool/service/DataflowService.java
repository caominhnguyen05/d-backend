package com.backend.dashboard_tool.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import com.backend.dashboard_tool.DTO.DataflowDTO;
import com.backend.dashboard_tool.database.DataflowRepository;
import com.backend.dashboard_tool.entity.Process_Data.Dataflow;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.sipocrecords.DataflowProcessPair;
import com.backend.dashboard_tool.sipocrecords.ProcessDataflowPair;

import java.util.*;

@Service
public class DataflowService {
    private final DataflowRepository dataflowRepository;
    private final ProcessService processService;

    public DataflowService(DataflowRepository dataflowRepository, ProcessService processService) {
        this.dataflowRepository = dataflowRepository;
        this.processService = processService;
    }

    /**
     * Retrieves dataflows based on the specified criteria.
     *
     * @param parentId the ID of the parent process (optional)
     * @param type the type of the process (optional)
     * @return a list of Dataflow objects matching the criteria
     */
    @Cacheable(value = "dataflows", key = "#parentId + '-' + #type")
    @Transactional(readOnly = true)
    public Iterable<Dataflow> getDataflows(
        @RequestParam(required = false) Long parentId,
        @RequestParam(required = false) String type
    ){
        List<ProcessEntity> processes = processService.getSubProcesses(parentId, type);
        List<Long> processIds = processes.stream()
            .map(ProcessEntity::getId)
            .toList();

        return dataflowRepository.findByProcessIds(processIds);
    }

    /**
     * Creates a new Dataflow object based on the provided DataflowDTO.
     * 
     * @param dataflowDTO the Dataflow object to create
     * @return the created Dataflow object
     */
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "dataflows", allEntries = true),
        @CacheEvict(value = "inputDataflows", allEntries = true),
        @CacheEvict(value = "outputDataflows", allEntries = true)
    })
    public Dataflow createDataflow(DataflowDTO dataflowDTO) {
        if(dataflowDTO.source() == dataflowDTO.target()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Source and target process IDs must be different.");
        }
        ProcessEntity sourceProcess = processService.getProcessById(dataflowDTO.source());
        if (sourceProcess == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Source process not found with ID: " + dataflowDTO.source());
        }
        ProcessEntity targetProcess = processService.getProcessById(dataflowDTO.target());
        if (targetProcess == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Target process not found with ID: " + dataflowDTO.target());
        }
        
        Dataflow dataflow = new Dataflow();
        dataflow.setName(dataflowDTO.name());
        dataflow.setSoort(dataflowDTO.soort());
        dataflow.setInternal(dataflowDTO.internal());
        dataflow.setDescription(dataflowDTO.description());
        dataflow.setSourceProcess(sourceProcess);
        dataflow.setTargetProcess(targetProcess);

        return dataflowRepository.save(dataflow);
    } 

    /**
     * Retrieves input dataflows for a specific process and type.
     * 
     * @param processId The id of the source process of the dataflow.
     * @param type The type of the process to filter out, so that the dataflows already present in the view are not shown again.
     * @return An iterable of ProcessDataflowPair objects, each containing a process and its corresponding dataflow.
     */
    @Cacheable(value = "inputDataflows", key = "#processId + '-' + #parentId  + '-' + #type")
    @Transactional(readOnly = true)
    public Iterable<ProcessDataflowPair> getInputDataflows(
        @RequestParam Long processId,
        @RequestParam(required = false) Long parentId,
        @RequestParam String type
    ) {
        List<Dataflow> dataflows = dataflowRepository.findInputDataflows(processId, parentId, type);
        return dataflows.stream()
            .map(dataflow -> new ProcessDataflowPair(dataflow.getSourceProcess().copy(), dataflow))
            .toList();
    }

    /**
     * Retrieves output dataflows for a specific process and type.
     * 
     * @param processId The id of the target process of the dataflow.
     * @param type The type of the process to filter out, so that the dataflows already present in the view are not shown again.
     * @return An iterable of ProcessDataflowPair objects, each containing a process and its corresponding dataflow.
     */
    @Cacheable(value = "outputDataflows", key = "#processId + '-' + #parentId + '-' + #type")
    @Transactional(readOnly = true)
    public Iterable<DataflowProcessPair> getOutputDataflows(
        @RequestParam Long processId,
        @RequestParam(required = false) Long parentId,
        @RequestParam String type
    ) {
        List<Dataflow> dataflows = dataflowRepository.findOutputDataflows(processId, parentId, type);
        return dataflows.stream()
            .map(dataflow -> new DataflowProcessPair(dataflow, dataflow.getTargetProcess().copy()))
            .toList();
    }
}
