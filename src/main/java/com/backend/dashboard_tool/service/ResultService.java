package com.backend.dashboard_tool.service;

import com.backend.dashboard_tool.DTO.ResultDTO;
import com.backend.dashboard_tool.database.ResultRepository;
import com.backend.dashboard_tool.database.People.OrganizationRepository;
import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Process_Data.Result;
import com.backend.dashboard_tool.sipocrecords.ResultOrgPair;

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
public class ResultService {

    private final ResultRepository resultRepository;
    private final OrganizationRepository organizationRepository;
    private final ProcessService processService;
    public ResultService(ResultRepository resultRepository, OrganizationRepository organizationRepository, ProcessService processService) {
        this.resultRepository = resultRepository;
        this.organizationRepository = organizationRepository;
        this.processService = processService;
    }
    /**
     * Retrieves triggers based on the provided process ID.
     * This method is used to get the external inputs/triggers for the left sipoc.
     * @param processId
     * @return an Iterable of OrgTriggerPair objects, each containing a Trigger and the corresponding organization.
     */
    @Cacheable(value = "results", key = "#processId")
    @Transactional(readOnly = true)
    public Iterable<ResultOrgPair> getResultsByProcessId(Long processId) {
        List<Result> results = resultRepository.findBySourceProcessId(processId);
        return results.stream()
            .map(result -> {
                Organization org = result.getTargetOrganization().copy();
                return new ResultOrgPair(result, org);
            }).toList();
    }

    /**
     * Creates a new Result object based on the provided ResultDTO.
     * 
     * @param resultDTO the Result object to create
     * @return the created Result object
     */
    @CacheEvict(value = "results", key = "#resultDTO.source()")
    @Transactional
    public Result createResult(ResultDTO resultDTO) {
        Organization targetOrganization = organizationRepository.getReferenceById(resultDTO.target());
        if (targetOrganization == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Target organization not found with ID: " + resultDTO.target());
        }
        ProcessEntity sourceProcess = processService.getProcessById(resultDTO.source());
        if (sourceProcess == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Source process not found with ID: " + resultDTO.source());
        }

        Result result = new Result();
        result.setName(resultDTO.name());
        result.setSoort(resultDTO.soort());
        result.setInternal(resultDTO.internal());
        result.setDescription(resultDTO.description());
        result.setSourceProcess(sourceProcess);
        result.setTargetOrganization(targetOrganization);

        return resultRepository.save(result);
    } 
}

