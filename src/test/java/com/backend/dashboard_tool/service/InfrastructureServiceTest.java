package com.backend.dashboard_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.Assets.AssetRepository;
import com.backend.dashboard_tool.database.Assets.FacilityRepository;
import com.backend.dashboard_tool.database.Assets.LocationRepository;
import com.backend.dashboard_tool.entity.Assets_Facilities.Asset;
import com.backend.dashboard_tool.entity.Assets_Facilities.Facility;
import com.backend.dashboard_tool.entity.Assets_Facilities.Location;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InfrastructureServiceTest {
    @Mock
    private AssetRepository assetRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ProcessService processService;

    @InjectMocks
    private InfrastructureService infrastructureService;

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

    /**
     * Creates a mock Facility object for testing purposes.
     * @param id
     * @param name
     * @return Facility object with specified id and name
     */
    private Facility createFacility(Long id, String name) {
        Facility facility = new Facility();
        facility.setId(id);
        facility.setName(name);
        return facility;
    }

    /**
     * Creates a mock Location object for testing purposes.
     * @param id
     * @param name
     * @return Location object with specified id and name
     */
    private Location createLocation(Long id, String name) {
        Location location = new Location();
        location.setId(id);
        location.setName(name);
        return location;
    }

    // --- /all endpoint tests ---

    @Test
    void getAllAssets() throws Exception {
        List<Asset> assets = List.of(createAsset(1L, "Asset 1"));
        when(assetRepository.findAll()).thenReturn(assets);

        List<?> result = infrastructureService.getAllInfrastructuresByType("asset");
        assertEquals(assets, result);
        verify(assetRepository, times(1)).findAll();
    }

    @Test
    void getAllFacilities() throws Exception {
        List<Facility> facilities = List.of(createFacility(1L, "Facility 1"));
        when(facilityRepository.findAll()).thenReturn(facilities);

        List<?> result = infrastructureService.getAllInfrastructuresByType("facility");
        assertEquals(facilities, result);
        verify(facilityRepository, times(1)).findAll();
    }

    @Test
    void getAllLocations() throws Exception {
        List<Location> locations = List.of(createLocation(1L, "Location 1"));
        when(locationRepository.findAll()).thenReturn(locations);

        List<?> result = infrastructureService.getAllInfrastructuresByType("location");
        assertEquals(locations, result);
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void getAllInfrastructuresByType_UnknownType() throws Exception {
        assertThrows(ResponseStatusException.class, () -> {
            infrastructureService.getAllInfrastructuresByType("unknown");
        });
    }

    // --- /process endpoint tests ---

    @Test
    void getAssetsByProcessId() throws Exception {
        Long processId = 10L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Asset> assets = List.of(createAsset(1L, "Asset 1"));
        when(assetRepository.findByProcessId(processId)).thenReturn(assets);

        List<?> result = infrastructureService.getInfrastructuresByTypeAndProcessId("asset", processId);
        assertEquals(assets, result);
        verify(assetRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getFacilitiesByProcessId() throws Exception {
        Long processId = 11L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Facility> facilities = List.of(createFacility(1L, "Facility 1"));
        when(facilityRepository.findByProcessId(processId)).thenReturn(facilities);

        List<?> result = infrastructureService.getInfrastructuresByTypeAndProcessId("facility", processId);
        assertEquals(facilities, result);
        verify(facilityRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getLocationsByProcessId() throws Exception {
        Long processId = 12L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Location> locations = List.of(createLocation(1L, "Location 1"));
        when(locationRepository.findByProcessId(processId)).thenReturn(locations);

        List<?> result = infrastructureService.getInfrastructuresByTypeAndProcessId("location", processId);
        assertEquals(locations, result);
        verify(locationRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getInfrastructureByTypeAndProcessId_ProcessNotFound() throws Exception {
        Long processId = 13L;
        when(processService.getProcessById(processId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            infrastructureService.getInfrastructuresByTypeAndProcessId("asset", processId);
        });
    }

    @Test
    void getAssetByTypeAndProcessId_UnknownType() throws Exception {
        Long processId = 14L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        assertThrows(ResponseStatusException.class, () -> {
            infrastructureService.getInfrastructuresByTypeAndProcessId("unknown", processId);
        });
    }
}
