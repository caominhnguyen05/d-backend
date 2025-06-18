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

import com.backend.dashboard_tool.entity.Assets_Facilities.Asset;
import com.backend.dashboard_tool.service.InfrastructureService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(InfrastructureController.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class InfrastructureControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InfrastructureService infrastructureService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates a mock Asset object for testing purposes.
     * @param id
     * @param name
     * @return Asset object with specified id and name
     */
    private Asset createAsset(Long id, String name) {
        Asset asset = new Asset();
        asset.setId(id);
        asset.setName(name);
        return asset;
    }

    // --- /all endpoint tests ---

    @Test
    void getAllAssets() throws Exception {
        List<Asset> assets = List.of(createAsset(1L, "Asset 1"));
        when(infrastructureService.getAllInfrastructuresByType("asset")).thenReturn((List) assets);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/infrastructure/all?type=asset")).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(assets), result.getResponse().getContentAsString());
        verify(infrastructureService, times(1)).getAllInfrastructuresByType("asset");
    }

    @Test
    void getAllInfrastructuresByType_UnknownType() throws Exception {
        when(infrastructureService.getAllInfrastructuresByType("unknown")).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown Infrastructure type: unknown"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/infrastructure/all?type=unknown")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getAllInfrastructuresByTypeEmpty() throws Exception {
        when(infrastructureService.getAllInfrastructuresByType("asset")).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/infrastructure/all?type=asset")).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    // --- /process endpoint tests ---

    @Test
    void getAssetsByProcessId() throws Exception {
        Long processId = 10L;
        List<Asset> assets = List.of(createAsset(1L, "Asset 1"));
        when(infrastructureService.getInfrastructuresByTypeAndProcessId("asset", processId)).thenReturn((List) assets);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/infrastructure/process?type=asset&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(assets), result.getResponse().getContentAsString());
        verify(infrastructureService, times(1)).getInfrastructuresByTypeAndProcessId("asset", processId);
    }

    @Test
    void getInfrastructuresByTypeAndProcessId_UnknownType() throws Exception {
        when(infrastructureService.getInfrastructuresByTypeAndProcessId("unknown", 99L)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown Infrastructure type: unknown"));
        Long processId = 99L;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/infrastructure/process?type=unknown&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getInfrastructuresByTypeAndProcessIdEmpty() throws Exception {
        Long processId = 99L;
        when(infrastructureService.getInfrastructuresByTypeAndProcessId("asset", processId)).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/infrastructure/process?type=asset&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
