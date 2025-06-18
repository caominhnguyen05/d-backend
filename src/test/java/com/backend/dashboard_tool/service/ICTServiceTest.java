package com.backend.dashboard_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.Assets.ApplicationRepository;
import com.backend.dashboard_tool.database.Assets.SystemEntityRepository;
import com.backend.dashboard_tool.database.Process_Data.DataStoreRepository;
import com.backend.dashboard_tool.entity.Assets_Facilities.Application;
import com.backend.dashboard_tool.entity.Assets_Facilities.SystemEntity;
import com.backend.dashboard_tool.entity.Process_Data.DataStore;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ICTServiceTest {
    @Mock
    private DataStoreRepository dataStoreRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private SystemEntityRepository systemEntityRepository;

    @Mock
    private ProcessService processService;

    @InjectMocks
    private ICTService ictService;

    /**
     * Creates a mock DataStore object for testing purposes.
     * @param id
     * @param name
     * @return DataStore object with specified id and name
     */
    private DataStore createDataStore(Long id, String name) {
        DataStore ds = new DataStore();
        ds.setId(id);
        ds.setName(name);
        return ds;
    }

    /**
     * Creates a mock Application object for testing purposes.
     * @param id
     * @param name
     * @return Application object with specified id and name
     */
    private Application createApplication(Long id, String name) {
        Application app = new Application();
        app.setId(id);
        app.setName(name);
        return app;
    }

    /**
     * Creates a mock SystemEntity object for testing purposes.
     * @param id
     * @param name
     * @return SystemEntity object with specified id and name
     */
    private SystemEntity createSystemEntity(Long id, String name) {
        SystemEntity se = new SystemEntity();
        se.setId(id);
        se.setName(name);
        return se;
    }

    // --- /all endpoint tests ---

    @Test
    void getAllDataStores() throws Exception {
        List<DataStore> dataStores = List.of(createDataStore(1L, "DataStore 1"));
        when(dataStoreRepository.findAll()).thenReturn(dataStores);

       List<?> result = ictService.getAllIctsByType("datastore");
       assertEquals(dataStores, result);
        verify(dataStoreRepository, times(1)).findAll();
    }

    @Test
    void getAllApplications() throws Exception {
        List<Application> applications = List.of(createApplication(1L, "Application 1"));
        when(applicationRepository.findAll()).thenReturn(applications);

        List<?> result = ictService.getAllIctsByType("application");
        assertEquals(applications, result);
        verify(applicationRepository, times(1)).findAll();
    }

    @Test
    void getAllSystemEntities() throws Exception {
        List<SystemEntity> systems = List.of(createSystemEntity(1L, "SystemEntity 1"));
        when(systemEntityRepository.findAll()).thenReturn(systems);

        List<?> result = ictService.getAllIctsByType("system");
        assertEquals(systems, result);
        verify(systemEntityRepository, times(1)).findAll();
    }

    @Test
    void getAllIctsByType_UnknownType() throws Exception {
        assertThrows(ResponseStatusException.class, () -> {
            ictService.getAllIctsByType("unknown");
        });
    }

    // --- /process endpoint tests ---

    @Test
    void getDataStoresByProcessId() throws Exception {
        Long processId = 10L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<DataStore> dataStores = List.of(createDataStore(1L, "DataStore 1"));
        when(dataStoreRepository.findByProcessId(processId)).thenReturn(dataStores);

        List<?> result = ictService.getIctsByTypeAndProcessId("datastore", processId);
        assertEquals(dataStores, result);
        verify(dataStoreRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getApplicationsByProcessId() throws Exception {
        Long processId = 11L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Application> applications = List.of(createApplication(1L, "Application 1"));
        when(applicationRepository.findByProcessId(processId)).thenReturn(applications);

        List<?> result = ictService.getIctsByTypeAndProcessId("application", processId);
        assertEquals(applications, result);
        verify(applicationRepository, times(1)).findByProcessId(processId);
        verify(applicationRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getSystemEntitiesByProcessId() throws Exception {
        Long processId = 12L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<SystemEntity> systems = List.of(createSystemEntity(1L, "SystemEntity 1"));
        when(systemEntityRepository.findByProcessId(processId)).thenReturn(systems);

        List<?> result = ictService.getIctsByTypeAndProcessId("system", processId);
        assertEquals(systems, result);
        verify(systemEntityRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getIctsByTypeAndProcessId_UnknownProcessId() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            ictService.getIctsByTypeAndProcessId("datastore", processId);
        });
    }

    @Test
    void getIctsByTypeAndProcessId_UnknownType() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        assertThrows(ResponseStatusException.class, () -> {
            ictService.getIctsByTypeAndProcessId("unknown", processId);
        });
    }
}
