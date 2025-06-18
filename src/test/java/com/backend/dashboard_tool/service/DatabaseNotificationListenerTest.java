package com.backend.dashboard_tool.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseNotificationListenerTest {

    @Mock
    private CacheManager cacheManager;
    @Mock
    private DataSourceProperties dbProps;
    @Mock
    private Cache processCache;
    @Mock
    private Cache processesCache;
    @Mock
    private Cache dataflowsCache;
    @Mock
    private Cache inputDataflowsCache;
    @Mock
    private Cache outputDataflowsCache;
    @Mock
    private Cache resultCache;
    @Mock
    private Cache triggerCache;
    @Mock
    private Cache allCache;
    @Mock
    private Cache byProcessCache;

    private DatabaseNotificationListener listener;

    @BeforeEach
    void setUp() throws Exception {
        listener = new DatabaseNotificationListener();
        inject(listener, "cacheManager", cacheManager);
        inject(listener, "dbProps", dbProps);
    }

    private void inject(Object target, String field, Object value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void procUpdate() {
        when(cacheManager.getCache("process")).thenReturn(processCache);
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification("process:UPDATE:42");
        verify(processCache).evict(42L);
        verify(processesCache).clear();
        verifyNoInteractions(dataflowsCache, inputDataflowsCache, outputDataflowsCache);
    }

    @Test
    void procDelete() {
        when(cacheManager.getCache("process")).thenReturn(processCache);
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification("process:DELETE:99");
        verify(processCache).evict(99L);
        verify(processesCache).clear();
        verifyNoInteractions(dataflowsCache, inputDataflowsCache, outputDataflowsCache);
    }

    @Test
    void procInsert() {
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification("process:INSERT");
        verify(processCache, never()).evict(any());
        verify(processesCache).clear();
        verifyNoInteractions(dataflowsCache, inputDataflowsCache, outputDataflowsCache);
    }

    @Test
    void flowUpdate() {
        when(cacheManager.getCache("dataflows")).thenReturn(dataflowsCache);
        when(cacheManager.getCache("inputDataflows")).thenReturn(inputDataflowsCache);
        when(cacheManager.getCache("outputDataflows")).thenReturn(outputDataflowsCache);

        listener.handleNotification("information_flow:UPDATE:5");
        verify(dataflowsCache).clear();
        verify(inputDataflowsCache).clear();
        verify(outputDataflowsCache).clear();
        verifyNoInteractions(processCache, processesCache);
    }

    @Test
    void flowInsert() {
        when(cacheManager.getCache("dataflows")).thenReturn(dataflowsCache);
        when(cacheManager.getCache("inputDataflows")).thenReturn(inputDataflowsCache);
        when(cacheManager.getCache("outputDataflows")).thenReturn(outputDataflowsCache);

        listener.handleNotification("information_flow:INSERT");
        verify(dataflowsCache).clear();
        verify(inputDataflowsCache).clear();
        verify(outputDataflowsCache).clear();
        verifyNoInteractions(processCache, processesCache);
    }

    @Test
    void unknownTable() {
        listener.handleNotification("unknown_table:UPDATE:1");
        verifyNoInteractions(processCache, processesCache, dataflowsCache, inputDataflowsCache, outputDataflowsCache);
    }

    @Test
    void emptyPayload() {
        listener.handleNotification("");
        verifyNoInteractions(processCache, processesCache, dataflowsCache, inputDataflowsCache, outputDataflowsCache);
    }

    @Test
    void malformed() {
        listener.handleNotification("justonepart");
        verifyNoInteractions(processCache, processesCache, dataflowsCache, inputDataflowsCache, outputDataflowsCache);
    }

    @Test
    void procNonNumeric() {
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification("process:UPDATE:notanumber");
        verify(processCache, never()).evict(any());
        verify(processesCache).clear();
    }

    @Test
    void procNullId() {
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification("process:UPDATE:NULL");
        verify(processCache, never()).evict(any());
        verify(processesCache).clear();
    }

    @Test
    void procUpdateWhitespace() {
        when(cacheManager.getCache("process")).thenReturn(processCache);
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification(" process : UPDATE : 123 ");
        verify(processCache).evict(123L);
        verify(processesCache).clear();
    }

    @Test
    void flowDelete() {
        when(cacheManager.getCache("dataflows")).thenReturn(dataflowsCache);
        when(cacheManager.getCache("inputDataflows")).thenReturn(inputDataflowsCache);
        when(cacheManager.getCache("outputDataflows")).thenReturn(outputDataflowsCache);

        listener.handleNotification("information_flow:DELETE:7");
        verify(dataflowsCache).clear();
        verify(inputDataflowsCache).clear();
        verify(outputDataflowsCache).clear();
    }

    @Test
    void nullPayload() {
        listener.handleNotification(null);
        verifyNoInteractions(processCache, processesCache, dataflowsCache, inputDataflowsCache, outputDataflowsCache);
    }

    @Test
    void onlyColon() {
        listener.handleNotification(":");
        verifyNoInteractions(processCache, processesCache, dataflowsCache, inputDataflowsCache, outputDataflowsCache);
    }

    @Test
    void procEmptyId() {
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification("process:UPDATE: ");
        verify(processCache, never()).evict(any());
        verify(processesCache).clear();
    }

    @Test
    void procIdSpaces() {
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification("process:UPDATE:   abc   ");
        verify(processCache, never()).evict(any());
        verify(processesCache).clear();
    }

    @Test
    void procUnknownOp() {
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification("process:FOO:123");
        verify(processCache, never()).evict(any());
        verify(processesCache).clear();
    }

    @Test
    void procUpdateProcessCacheNull() {
        when(cacheManager.getCache("process")).thenReturn(null);
        when(cacheManager.getCache("processes")).thenReturn(processesCache);

        listener.handleNotification("process:UPDATE:42");
        verify(processCache, never()).evict(any());
        verify(processesCache).clear();
    }

    @Test
    void procUpdateProcessesCacheNull() {
        when(cacheManager.getCache("process")).thenReturn(processCache);
        when(cacheManager.getCache("processes")).thenReturn(null);

        listener.handleNotification("process:UPDATE:42");
        verify(processCache).evict(42L);
        verify(processesCache, never()).clear();
    }

    @Test
    void flowUpdateDataflowsCacheNull() {
        when(cacheManager.getCache("dataflows")).thenReturn(null);
        when(cacheManager.getCache("inputDataflows")).thenReturn(inputDataflowsCache);
        when(cacheManager.getCache("outputDataflows")).thenReturn(outputDataflowsCache);

        listener.handleNotification("information_flow:UPDATE:5");
        verify(inputDataflowsCache).clear();
        verify(outputDataflowsCache).clear();
        verify(dataflowsCache, never()).clear();
    }

    @Test
    void flowUpdateInputDataflowsCacheNull() {
        when(cacheManager.getCache("dataflows")).thenReturn(dataflowsCache);
        when(cacheManager.getCache("inputDataflows")).thenReturn(null);
        when(cacheManager.getCache("outputDataflows")).thenReturn(outputDataflowsCache);

        listener.handleNotification("information_flow:UPDATE:5");
        verify(dataflowsCache).clear();
        verify(outputDataflowsCache).clear();
        verify(inputDataflowsCache, never()).clear();
    }

    @Test
    void flowUpdateOutputDataflowsCacheNull() {
        when(cacheManager.getCache("dataflows")).thenReturn(dataflowsCache);
        when(cacheManager.getCache("inputDataflows")).thenReturn(inputDataflowsCache);
        when(cacheManager.getCache("outputDataflows")).thenReturn(null);

        listener.handleNotification("information_flow:UPDATE:5");
        verify(dataflowsCache).clear();
        verify(inputDataflowsCache).clear();
        verify(outputDataflowsCache, never()).clear();
    }
    
    @Test
    void triggerAndResultUpdate() {
        when(cacheManager.getCache("dataflows")).thenReturn(dataflowsCache);
        when(cacheManager.getCache("inputDataflows")).thenReturn(inputDataflowsCache);
        when(cacheManager.getCache("outputDataflows")).thenReturn(outputDataflowsCache);
        when(cacheManager.getCache("triggers")).thenReturn(triggerCache);
        when(cacheManager.getCache("results")).thenReturn(resultCache);

        listener.handleNotification("information_flow:UPDATE:5");
        verify(triggerCache, times(1)).clear();
        verify(resultCache, times(1)).clear();
    }
    @Test
    void controlsCacheUpdate() {
        when(cacheManager.getCache("controls")).thenReturn(allCache);
        when(cacheManager.getCache("controlByProcess")).thenReturn(byProcessCache);

        listener.handleNotification("audit:UPDATE");
        verify(allCache).clear();
        verify(byProcessCache).clear();
    }

    @Test
    void controlsNullCacheUpdate() {
        when(cacheManager.getCache("controls")).thenReturn(null);
        when(cacheManager.getCache("controlByProcess")).thenReturn(null);

        listener.handleNotification("audit:UPDATE");
        verifyNoInteractions(allCache, byProcessCache);
    }

    @Test
    void ictCacheUpdate() {
        when(cacheManager.getCache("icts")).thenReturn(allCache);
        when(cacheManager.getCache("ictByProcess")).thenReturn(byProcessCache);

        listener.handleNotification("data_store:UPDATE");
        verify(allCache).clear();
        verify(byProcessCache).clear();
    }

    @Test
    void ictNullCacheUpdate() {
        when(cacheManager.getCache("icts")).thenReturn(null);
        when(cacheManager.getCache("ictByProcess")).thenReturn(null);

        listener.handleNotification("data_store:UPDATE");
        verifyNoInteractions(allCache, byProcessCache);
    }

    @Test
    void infrastructureCacheUpdate() {
        when(cacheManager.getCache("infrastructures")).thenReturn(allCache);
        when(cacheManager.getCache("infrastructureByProcess")).thenReturn(byProcessCache);

        listener.handleNotification("asset:UPDATE");
        verify(allCache).clear();
        verify(byProcessCache).clear();
    }

    @Test
    void infrastructureNullCacheUpdate() {
        when(cacheManager.getCache("infrastructures")).thenReturn(null);
        when(cacheManager.getCache("infrastructureByProcess")).thenReturn(null);

        listener.handleNotification("asset:UPDATE");
        verifyNoInteractions(allCache, byProcessCache);
    }

    @Test
    void initiativeCacheUpdate() {
        when(cacheManager.getCache("initiatives")).thenReturn(allCache);
        when(cacheManager.getCache("initiativeByProcess")).thenReturn(byProcessCache);

        listener.handleNotification("improvement:UPDATE");
        verify(allCache).clear();
        verify(byProcessCache).clear();
    }

    @Test
    void initiativeNullCacheUpdate() {
        when(cacheManager.getCache("initiatives")).thenReturn(null);
        when(cacheManager.getCache("initiativeByProcess")).thenReturn(null);

        listener.handleNotification("improvement:UPDATE");
        verifyNoInteractions(allCache, byProcessCache);
    }

    @Test
    void peopleCacheUpdate() {
        when(cacheManager.getCache("people")).thenReturn(allCache);
        when(cacheManager.getCache("peopleByProcess")).thenReturn(byProcessCache);

        listener.handleNotification("department:UPDATE");
        verify(allCache).clear();
        verify(byProcessCache).clear();
    }

    @Test
    void peopleNullCacheUpdate() {
        when(cacheManager.getCache("people")).thenReturn(null);
        when(cacheManager.getCache("peopleByProcess")).thenReturn(null);

        listener.handleNotification("department:UPDATE");
        verifyNoInteractions(allCache, byProcessCache);
    }

    @Test
    void performanceCacheUpdate() {
        when(cacheManager.getCache("performances")).thenReturn(allCache);
        when(cacheManager.getCache("performanceByProcess")).thenReturn(byProcessCache);

        listener.handleNotification("kpi:UPDATE");
        verify(allCache).clear();
        verify(byProcessCache).clear();
    }   

    @Test
    void performanceNullCacheUpdate() {
        when(cacheManager.getCache("performances")).thenReturn(null);
        when(cacheManager.getCache("performanceByProcess")).thenReturn(null);

        listener.handleNotification("kpi:UPDATE");
        verifyNoInteractions(allCache, byProcessCache);
    }

    @Test
    void relevanceCacheUpdate() {
        when(cacheManager.getCache("relevances")).thenReturn(allCache);
        when(cacheManager.getCache("relevanceByProcess")).thenReturn(byProcessCache);

        listener.handleNotification("cost:UPDATE");
        verify(allCache).clear();
        verify(byProcessCache).clear();
    }  
    
    @Test
    void relevanceNullCacheUpdate() {
        when(cacheManager.getCache("relevances")).thenReturn(null);
        when(cacheManager.getCache("relevanceByProcess")).thenReturn(null);

        listener.handleNotification("cost:UPDATE");
        verifyNoInteractions(allCache, byProcessCache);
    }
}