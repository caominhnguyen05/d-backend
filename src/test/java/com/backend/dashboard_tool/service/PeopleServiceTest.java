package com.backend.dashboard_tool.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.People.DepartmentRepository;
import com.backend.dashboard_tool.database.People.EmployeeRepository;
import com.backend.dashboard_tool.database.People.FunctionRepository;
import com.backend.dashboard_tool.database.People.OrganizationRepository;
import com.backend.dashboard_tool.database.People.RoleRepository;
import com.backend.dashboard_tool.database.People.ConsultationRepository;
import com.backend.dashboard_tool.entity.People.Department;
import com.backend.dashboard_tool.entity.People.Employee;
import com.backend.dashboard_tool.entity.People.Function;
import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.People.Role;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.People.Consultation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PeopleServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private FunctionRepository functionRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ConsultationRepository consultationRepository;

    @Mock
    private ProcessService processService;

    @InjectMocks
    private PeopleService peopleService;

    /**
     * Helper method to create a Department object.
     * 
     * @param id   the ID of the department
     * @param name the name of the department
     * @return a Department object with the specified parameters
     */
    private Department createDepartment(Long id, String name) {
        Department dept = new Department();
        dept.setId(id);
        dept.setName(name);
        return dept;
    }

    /**
     * Helper method to create an Organization object.
     * 
     * @param id   the ID of the organization
     * @param name the name of the organization
     * @return an Organization object with the specified parameters
     */
    private Organization createOrganization(Long id, String name) {
        Organization org = new Organization();
        org.setId(id);
        org.setName(name);
        return org;
    }

    /**
     * Helper method to create an Employee object.
     * 
     * @param id   the ID of the employee
     * @param name the name of the employee
     * @return an Employee object with the specified parameters
     */
    private Employee createEmployee(Long id, String name) {
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName(name);
        return emp;
    }

    /**
     * Helper method to create a Function object.
     * 
     * @param id   the ID of the function
     * @param name the name of the function
     * @return a Function object with the specified parameters
     */
    private Function createFunction(Long id, String name) {
        Function f = new Function();
        f.setId(id);
        f.setName(name);
        return f;
    }

    /**
     * Helper method to create a Role object.
     * 
     * @param id   the ID of the role
     * @param name the name of the role
     * @return a Role object with the specified parameters
     */
    private Role createRole(Long id, String name) {
        Role r = new Role();
        r.setId(id);
        r.setName(name);
        return r;
    }

    /**
     * Helper method to create a Consultation object.
     * 
     * @param id   the ID of the consultation
     * @param name the name of the consultation
     * @return a Consultation object with the specified parameters
     */
    private Consultation createConsultation(Long id, String name) {
        Consultation c = new Consultation();
        c.setId(id);
        c.setName(name);
        return c;
    }

    // --- /all endpoint tests ---

    /**
     * Test method for the getAllPeopleByType() method in PeopleController.
     * This test checks if the method returns the expected departments.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getAllDepartments() throws Exception {
        List<Department> departments = List.of(createDepartment(1L, "Dept 1"));
        when(departmentRepository.findAll()).thenReturn(departments);

        List<?> result = peopleService.getAllPeopleByType("department");
        assertEquals(departments, result);
        verify(departmentRepository, times(1)).findAll();
    }

    /**
     * Test method for the getAllPeopleByType() method in PeopleController.
     * This test checks if the method returns the expected employees.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getAllEmployees() throws Exception {
        List<Employee> employees = List.of(createEmployee(1L, "Emp 1"));
        when(employeeRepository.findAll()).thenReturn(employees);

        List<?> result = peopleService.getAllPeopleByType("employee");
        assertEquals(employees, result);
        verify(employeeRepository, times(1)).findAll();
    }

    /**
     * Test method for the getAllPeopleByType() method in PeopleController.
     * This test checks if the method returns the expected functions.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getAllFunctions() throws Exception {
        List<Function> functions = List.of(createFunction(1L, "Func 1"));
        when(functionRepository.findAll()).thenReturn(functions);

        List<?> result = peopleService.getAllPeopleByType("function");
        assertEquals(functions, result);
        verify(functionRepository, times(1)).findAll();
    }

    /**
     * Test method for the getAllPeopleByType() method in PeopleController.
     * This test checks if the method returns the expected organizations.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getAllOrganizations() throws Exception {
        List<Organization> orgs = List.of(createOrganization(1L, "Org 1"));
        when(organizationRepository.findAll()).thenReturn(orgs);

        List<?> result = peopleService.getAllPeopleByType("organization");
        assertEquals(orgs, result);
        verify(organizationRepository, times(1)).findAll();
    }

    /**
     * Test method for the getAllPeopleByType() method in PeopleController.
     * This test checks if the method returns the expected roles.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getAllRoles() throws Exception {
        List<Role> roles = List.of(createRole(1L, "Role 1"));
        when(roleRepository.findAll()).thenReturn(roles);

        List<?> result = peopleService.getAllPeopleByType("role");
        assertEquals(roles, result);
        verify(roleRepository, times(1)).findAll();
    }

    /**
     * Test method for the getAllPeopleByType() method in PeopleController.
     * This tests checks if the method returns the expected consultations.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getAllConsultations() throws Exception {
        List<Consultation> consultations = List.of(createConsultation(1L, "Consultation 1"));
        when(consultationRepository.findAll()).thenReturn(consultations);

        List<?> result = peopleService.getAllPeopleByType("consultation");
        assertEquals(consultations, result);
        verify(consultationRepository, times(1)).findAll();
    }

    /**
     * Test method for the getAllPeopleByType() method in PeopleController.
     * This test checks if the method returns a bad request for an unknown type.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getAllPeopleByType_UnknownType() throws Exception {
        assertThrows(ResponseStatusException.class, () -> {
            peopleService.getAllPeopleByType("unknown");
        });
    }

    // --- /process endpoint tests ---

    /**
     * Test method for the getPeopleByTypeAndProcessId() method in PeopleController.
     * This test checks if the method returns the expected departments for a given processId.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getDepartmentsByProcessId() throws Exception {
        Long processId = 10L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Department> departments = List.of(createDepartment(1L, "Dept 1"));
        when(departmentRepository.findByProcessId(processId)).thenReturn(departments);

        List<?> result = peopleService.getPeopleByTypeAndProcessId("department", processId);
        assertEquals(departments, result);
        verify(departmentRepository, times(1)).findByProcessId(processId);
    }

    /**
     * Test method for the getPeopleByTypeAndProcessId() method in PeopleController.
     * This test checks if the method returns the expected employees for a given processId.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getEmployeesByProcessId() throws Exception {
        Long processId = 11L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Employee> employees = List.of(createEmployee(1L, "Emp 1"));
        when(employeeRepository.findByProcessId(processId)).thenReturn(employees);

        List<?> result = peopleService.getPeopleByTypeAndProcessId("employee", processId);
        assertEquals(employees, result);
        verify(employeeRepository, times(1)).findByProcessId(processId);
    }

    /**
     * Test method for the getPeopleByTypeAndProcessId() method in PeopleController.
     * This test checks if the method returns the expected functions for a given processId.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getFunctionsByProcessId() throws Exception {
        Long processId = 12L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Function> functions = List.of(createFunction(1L, "Func 1"));
        when(functionRepository.findByProcessId(processId)).thenReturn(functions);

        List<?> result = peopleService.getPeopleByTypeAndProcessId("function", processId);
        assertEquals(functions, result);
        verify(functionRepository, times(1)).findByProcessId(processId);
    }

    /**
     * Test method for the getPeopleByTypeAndProcessId() method in PeopleController.
     * This test checks if the method returns the expected organizations for a given processId.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getOrganizationsByProcessId() throws Exception {
        Long processId = 13L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Organization> orgs = List.of(createOrganization(1L, "Org 1"));
        when(organizationRepository.findByProcessId(processId)).thenReturn(orgs);

        List<?> result = peopleService.getPeopleByTypeAndProcessId("organization", processId);
        assertEquals(orgs, result);
        verify(organizationRepository, times(1)).findByProcessId(processId);
    }

    /**
     * Test method for the getPeopleByTypeAndProcessId() method in PeopleController.
     * This test checks if the method returns the expected roles for a given processId.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getRolesByProcessId() throws Exception {
        Long processId = 14L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Role> roles = List.of(createRole(1L, "Role 1"));
        when(roleRepository.findByProcessId(processId)).thenReturn(roles);

        List<?> result = peopleService.getPeopleByTypeAndProcessId("role", processId);
        assertEquals(roles, result);
        verify(roleRepository, times(1)).findByProcessId(processId);
    }

     /**
     * Test method for the getConsultationByTypeAndProcessId() method in PeopleController.
     * This test checks if the method returns the expected consultations for a given processId.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getConsultationsByProcessId() throws Exception {
        Long processId = 15L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Consultation> consultations = List.of(createConsultation(1L, "Consultation 1"));
        when(consultationRepository.findByProcessId(processId)).thenReturn(consultations);

        List<?> result = peopleService.getPeopleByTypeAndProcessId("consultation", processId);
        assertEquals(consultations, result);
        verify(consultationRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getPeopleServicesByTypeAndProcessId_UnknownProcessId() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            peopleService.getPeopleByTypeAndProcessId("improvement", processId);
        });
    }

    /**
     * Test method for the getPeopleByTypeAndProcessId() method in PeopleController.
     * This test checks if the method returns a bad request for an unknown type.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getPeopleByTypeAndProcessId_UnknownType() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        assertThrows(ResponseStatusException.class, () -> {
            peopleService.getPeopleByTypeAndProcessId("unknown", processId);
        });
    }
}
