package com.backend.dashboard_tool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.backend.dashboard_tool.entity.Process_Data.Trigger;
import com.backend.dashboard_tool.DTO.TriggerDTO;
import com.backend.dashboard_tool.sipocrecords.OrgTriggerPair;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.backend.dashboard_tool.service.TriggerService;

/**
 * Controller for handling trigger-related requests.
 * this class provides an endpoint for retrieving triggers based on a (target) process ID.
 */
@RestController
@CrossOrigin(origins = "${frontend.url}")	
@RequestMapping("/trigger")
public class TriggerController {
    private final TriggerService triggerService;

    public TriggerController(TriggerService triggerService) {
        this.triggerService = triggerService;
    }

    /**
     * Endpoint for retrieving triggers based on a target process ID.
     * The main use case is to get the external inputs/triggers for the left sipoc pane.
     * 
     * @param processId the ID of the target process that takes the triggers as input.
     * @return a list of OrgTriggerPair objects, each containing a Trigger and the corresponding organization.
     */
    @GetMapping("/supplierinput")
    public ResponseEntity<Iterable<OrgTriggerPair>> getTriggers(
        @RequestParam Long processId
    ){
        Iterable<OrgTriggerPair> triggers = triggerService.getTriggersByProcessId(processId);
        if (triggers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(triggers);
    }

    /**
     * Endpoint for creating a new trigger.
     * @param trigger the Trigger object to create
     * @return the created Trigger object
     */
    @PostMapping("/create")
    public ResponseEntity<Trigger> createTrigger(@Validated @RequestBody TriggerDTO triggerDTO) {
        Trigger trigger = triggerService.createTrigger(triggerDTO);
        if (trigger == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(trigger);
    }
}
