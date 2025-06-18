package com.backend.dashboard_tool.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.entity.People.Department;
import com.backend.dashboard_tool.service.PeopleService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for PeopleController.
 * This class contains unit tests for the PeopleController methods.
 */
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PeopleController.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class PeopleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PeopleService peopleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        when(peopleService.getAllPeopleByType("department")).thenReturn((List) departments);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/people/all?type=department")).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(departments), result.getResponse().getContentAsString());
        verify(peopleService, times(1)).getAllPeopleByType("department");
    }

    /**
     * Test method for the getAllPeopleByType() method in PeopleController.
     * This test checks if the method returns a bad request for an unknown type.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getAllPeopleByType_UnknownType() throws Exception {
        when(peopleService.getAllPeopleByType("unknown")).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown people type: unknown"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/people/all?type=unknown")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getAllPeopleByTypeEmpty() throws Exception {
        when(peopleService.getAllPeopleByType("department")).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/people/all?type=department")).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
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
        List<Department> departments = List.of(createDepartment(1L, "Dept 1"));
        when(peopleService.getPeopleByTypeAndProcessId("department", processId)).thenReturn((List) departments);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/people/process?type=department&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(departments), result.getResponse().getContentAsString());
        verify(peopleService, times(1)).getPeopleByTypeAndProcessId("department", processId);
    }

    /**
     * Test method for the getPeopleByTypeAndProcessId() method in PeopleController.
     * This test checks if the method returns a bad request for an unknown type.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getPeopleByTypeAndProcessId_UnknownType() throws Exception {
        when(peopleService.getPeopleByTypeAndProcessId("unknown", 99L)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown people type: unknown"));
        Long processId = 99L;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/people/process?type=unknown&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getPeopleByTypeAndProcessIdEmpty() throws Exception {
        Long processId = 99L;
        when(peopleService.getPeopleByTypeAndProcessId("department", processId)).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/people/process?type=department&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
