package com.backend.dashboard_tool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.database.People.ConsultationRepository;
import com.backend.dashboard_tool.database.People.DepartmentRepository;
import com.backend.dashboard_tool.database.People.EmployeeRepository;
import com.backend.dashboard_tool.database.People.FunctionRepository;
import com.backend.dashboard_tool.database.People.OrganizationRepository;
import com.backend.dashboard_tool.database.People.RoleRepository;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

@Service
public class PeopleService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final FunctionRepository functionRepository;
    private final OrganizationRepository organizationRepository;
    private final RoleRepository roleRepository;
    private final ConsultationRepository consultationRepository;
    private final ProcessService processService;

    /**
     * Constructor for PeopleService.
     *
     * @param departmentRepository the DepartmentRepository instance
     * @param employeeRepository the EmployeeRepository instance
     * @param functionRepository the FunctionRepository instance
     * @param organizationRepository the OrganizationRepository instance
     * @param roleRepository the RoleRepository instance
     * @param consultationRepository the ConsultationRepository instance
     * @param processService the ProcessService instance
     */
    public PeopleService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, FunctionRepository functionRepository, OrganizationRepository organizationRepository, RoleRepository roleRepository, ConsultationRepository consultationRepository, ProcessService processService) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.functionRepository = functionRepository;
        this.organizationRepository = organizationRepository;
        this.roleRepository = roleRepository;
        this.consultationRepository = consultationRepository;
        this.processService = processService;
    }

    /**
     * Retrieves all people-related entities of a specific type.
     * This method fetches all records for the specified people type.
     * @param type
     * @return A list of entities for the specified type, or throws an exception if the type is unknown.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "people", key = "#type")
    public List<?> getAllPeopleByType(String type) {
        return switch (type.toLowerCase()) {
            case "department" -> departmentRepository.findAll();
            case "employee" -> employeeRepository.findAll();
            case "function" -> functionRepository.findAll();
            case "organization" -> organizationRepository.findAll();
            case "role" -> roleRepository.findAll();
            case "consultation" -> consultationRepository.findAll();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown people type: " + type);
        };
    }

    /**
     * Retrieves a specific initiative entity by type and process ID.
     * This method fetches a single entity corresponding to the specified people type and process ID.
     *
     * @param type The type of people to retrieve (e.g., "employee", "department").
     * @param processId The ID of the process associated with the people entity.
     * @return A list of matching entities for the specified people type and process ID,
     *         or throws an exception if no such entities exist.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "peopleByProcess", key = "#type + '-' + #processId")
    public List<?> getPeopleByTypeAndProcessId(String type, Long processId) {
        ProcessEntity process = processService.getProcessById(processId);
        if (process == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process with ID " + processId + " not found.");
        }

        return switch (type.toLowerCase()) {
            case "employee" -> employeeRepository.findByProcessId(processId);
            case "department" -> departmentRepository.findByProcessId(processId);
            case "function" -> functionRepository.findByProcessId(processId);
            case "organization" -> organizationRepository.findByProcessId(processId);
            case "role" -> roleRepository.findByProcessId(processId);
            case "consultation" -> consultationRepository.findByProcessId(processId);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown people type: " + type);
        };
    }
}
