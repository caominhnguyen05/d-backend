package com.backend.dashboard_tool.controller;

import com.backend.dashboard_tool.entity.Process_Data.Dataflow;
import com.backend.dashboard_tool.service.DataflowService;
import com.backend.dashboard_tool.sipocrecords.DataflowProcessPair;
import com.backend.dashboard_tool.sipocrecords.ProcessDataflowPair;
import com.backend.dashboard_tool.DTO.DataflowDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling dataflow-related requests.
 * This class provides an endpoint for retrieving dataflows based on process IDs and types.
 */
@RestController
@CrossOrigin(origins = "${frontend.url}")	
@RequestMapping("/dataflow")
public class DataflowController {
    
    private final DataflowService dataflowService;

    /**
     * Constructor for DataflowController.
     * 
     * @param dataflowService the DataflowService instance
     */
    public DataflowController(DataflowService dataflowService) {
        this.dataflowService = dataflowService;
    }

    /**
     * Endpoint for retrieving dataflows based on process IDs and types.
     * Cross-origin requests are allowed from ${frontend.url}.
     * 
     * @param parentId the ID of the parent process (optional)
     * @param type the type of the process (optional)
     * @return a list of Dataflow objects matching the criteria
     */
    @GetMapping("/filter")
    public Iterable<Dataflow> getDataflows(
        @RequestParam(required = false) Long parentId,
        @RequestParam(required = false) String type
    ){
        return dataflowService.getDataflows(parentId, type);
    }

    /**
     * Endpoint for creating a new dataflow.
     * 
     * @param dataflow the Dataflow object to create
     * @return the created Dataflow object
     */
    @PostMapping("/create")
    public ResponseEntity<Dataflow> createDataflow(@Validated @RequestBody DataflowDTO dataflowDTO) {
        Dataflow dataflow = dataflowService.createDataflow(dataflowDTO);
        if (dataflow == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(dataflow);
    }

    /**
     * Endpoint for retrieving dataflows and corresponding source processes from different types.
     * 
     * @param processId The id of the target process of the dataflow.
     * @param parentId The id of the parent process to filter out, so that the dataflows already present in the view are not shown again.
     * @return An iterable of ProcessDataflowPair objects, each containing a process and its corresponding dataflow.
     */
    @GetMapping("/inputs")
    public Iterable<ProcessDataflowPair> getInputDataflows(
        @RequestParam Long processId,
        @RequestParam(required = false) Long parentId,
        @RequestParam String type
    ){
        return dataflowService.getInputDataflows(processId, parentId, type);
    }

    /**
     * Endpoint for retrieving dataflows and corresponding target processes from different types.
     * 
     * @param processId The id of the source process of the dataflow.
     * @param type The type of the process to filter out, so that the dataflows already present in the view are not shown again.
     * @return An iterable of DataflowProcessPair objects, each containing a process and its corresponding dataflow.
     */

    @GetMapping("/outputs")
    public Iterable<DataflowProcessPair> getOutputDataflows(
        @RequestParam Long processId,
        @RequestParam(required = false) Long parentId,
        @RequestParam String type
    ){
        return dataflowService.getOutputDataflows(processId, parentId, type);
    }
}
