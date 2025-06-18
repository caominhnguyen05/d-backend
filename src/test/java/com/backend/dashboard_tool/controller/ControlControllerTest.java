package com.backend.dashboard_tool.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.service.ControlService;
import com.backend.dashboard_tool.entity.Strategy.Audit;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ControlController.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class ControlControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ControlService controlService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Audit createAudit(Long id, String name) {
        Audit audit = new Audit();
        audit.setId(id);
        audit.setName(name);
        return audit;
    }

    // --- /all endpoint tests ---

    
    @Test
    void getAllAudits() throws Exception {
        List<?> audits = List.of(createAudit(1L, "Audit 1"));
         when(controlService.getAllControlsByType("audit")).thenReturn((List) audits);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/control/all?type=audit")).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(audits), result.getResponse().getContentAsString());
        verify(controlService, times(1)).getAllControlsByType("audit");
    }

    @Test
    void getAllControlsByType_UnknownType() throws Exception {
        when(controlService.getAllControlsByType("unknown")).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown control type: unknown"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/control/all?type=unknown")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getAllControlsByTypeEmpty() throws Exception {
        when(controlService.getAllControlsByType("audit")).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/control/all?type=audit")).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    // --- /process endpoint tests ---

    @Test
    void getAuditsByProcessId() throws Exception {
        Long processId = 10L;
        List<Audit> audits = List.of(createAudit(1L, "Audit 1"));
        when(controlService.getControlsByTypeAndProcessId("audit", processId)).thenReturn((List) audits);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/control/process?type=audit&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(audits), result.getResponse().getContentAsString());
        verify(controlService, times(1)).getControlsByTypeAndProcessId("audit", processId);
    }

    @Test
    void getControlByTypeAndProcessId_UnknownType() throws Exception {
        when(controlService.getControlsByTypeAndProcessId("unknown", 99L)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown control type: unknown"));
        Long processId = 99L;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/control/process?type=unknown&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getControlByTypeAndProcessIdEmpty() throws Exception {
        Long processId = 10L;
        when(controlService.getControlsByTypeAndProcessId("audit", processId)).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/control/process?type=audit&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
