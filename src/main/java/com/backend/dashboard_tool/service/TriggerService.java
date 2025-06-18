package com.backend.dashboard_tool.service;

import com.backend.dashboard_tool.DTO.TriggerDTO;
import com.backend.dashboard_tool.database.TriggerRepository;
import com.backend.dashboard_tool.database.People.OrganizationRepository;
import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Process_Data.Trigger;
import com.backend.dashboard_tool.sipocrecords.OrgTriggerPair;

import java.util.*;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
/**
 * Service class for managing triggers.
 * This class provides methods to retrieve and create triggers.
 */
@Service
public class TriggerService {

    private final TriggerRepository triggerRepository;
    private final OrganizationRepository organizationRepository;
    private final ProcessService processService;
    public TriggerService(TriggerRepository triggerRepository, OrganizationRepository organizationRepository, ProcessService processService) {
        this.triggerRepository = triggerRepository;
        this.organizationRepository = organizationRepository;
        this.processService = processService;
    }
    /**
     * Retrieves triggers based on the provided process ID.
     * This method is used to get the external inputs/triggers for the left sipoc.
     * @param processId
     * @return an Iterable of OrgTriggerPair objects, each containing a Trigger and the corresponding organization.
     */
    @Cacheable(value = "triggers", key = "#processId")
    @Transactional(readOnly = true)
    public Iterable<OrgTriggerPair> getTriggersByProcessId(Long processId) {
        List<Trigger> triggers = triggerRepository.findByTargetProcessId(processId);
        return triggers.stream()
            .map(trigger -> {
                Organization org = trigger.getSourceOrganization().copy();
                return new OrgTriggerPair(org, trigger);
            }).toList();
    }

    /**
     * Creates a new Trigger object based on the provided TriggerDTO.
     * 
     * @param triggerDTO the Trigger object to create
     * @return the created Trigger object
     */
    @CacheEvict(value = "triggers", key = "#triggerDTO.target()")
    @Transactional
    public Trigger createTrigger(TriggerDTO triggerDTO) {
        Organization sourceOrganization = organizationRepository.getReferenceById(triggerDTO.source());
        if (sourceOrganization == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Source organization not found with ID: " + triggerDTO.source());
        }
        ProcessEntity targetProcess = processService.getProcessById(triggerDTO.target());
        if (targetProcess == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Target process not found with ID: " + triggerDTO.target());
        }

        Trigger trigger = new Trigger();
        trigger.setName(triggerDTO.name());
        trigger.setSoort(triggerDTO.soort());
        trigger.setInternal(triggerDTO.internal());
        trigger.setDescription(triggerDTO.description());
        trigger.setSourceOrganization(sourceOrganization);
        trigger.setTargetProcess(targetProcess);

        return triggerRepository.save(trigger);
    } 
}

