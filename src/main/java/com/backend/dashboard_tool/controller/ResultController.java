package com.backend.dashboard_tool.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.backend.dashboard_tool.DTO.ResultDTO;
import com.backend.dashboard_tool.entity.Process_Data.Result;
import com.backend.dashboard_tool.service.ResultService;
import com.backend.dashboard_tool.sipocrecords.ResultOrgPair;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/result")
public class ResultController {
    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    /**
     * Endpoint for retrieving results based on a target process ID.
     * The main use case is to get the external outputs/results for the left sipoc pane.
     * 
     * @param processId the ID of the target process that takes the results as output.
     * @return a list of ResultOrgPair objects, each containing a Result and the corresponding organization.
     */
    @GetMapping("/outputconsumer")
    public ResponseEntity<Iterable<ResultOrgPair>> getResults(
        @RequestParam Long processId
    ){
        Iterable<ResultOrgPair> results = resultService.getResultsByProcessId(processId);
        if (results == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results);
    }

    /**
     * Endpoint for creating a new result.
     * 
     * @param result the Result object to create
     * @return the created Result object
     */
    @PostMapping("/create")
    public ResponseEntity<Result> createResult(@Validated @RequestBody ResultDTO resultDTO) {
        Result result = resultService.createResult(resultDTO);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
