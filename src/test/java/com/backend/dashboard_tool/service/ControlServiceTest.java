package com.backend.dashboard_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import com.backend.dashboard_tool.database.DocumentRepository;
import com.backend.dashboard_tool.database.Strategy.AuditRepository;
import com.backend.dashboard_tool.database.Strategy.LegislationRepository;
import com.backend.dashboard_tool.entity.Strategy.Audit;
import com.backend.dashboard_tool.entity.Strategy.Legislation;
import com.backend.dashboard_tool.entity.Document;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.web.server.ResponseStatusException;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ControlServiceTest {
    @Mock
    private AuditRepository auditRepository;

    @Mock
    private LegislationRepository legislationRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private ProcessService processService;

    @InjectMocks
    private ControlService controlService;

    /**
     * Creates a mock Audit object for testing purposes.
     * @param id
     * @param name
     * @return Audit object with specified id and name
     */
    private Audit createAudit(Long id, String name) {
        Audit audit = new Audit();
        audit.setId(id);
        audit.setName(name);
        return audit;
    }

    /**
     * Creates a mock Legislation object for testing purposes.
     * @param id
     * @param name
     * @return Legislation object with specified id and name
     */
    private Legislation createLegislation(Long id, String name) {
        Legislation legislation = new Legislation();
        legislation.setId(id);
        legislation.setName(name);
        return legislation;
    }

    /**
     * Creates a mock Document object for testing purposes.
     * @param id
     * @param name
     * @return Document object with specified id and name
     */
    private Document createDocument(Long id, String name) {
        Document document = new Document();
        document.setId(id);
        document.setName(name);
        return document;
    }

    // --- /all methods tests ---

    @Test
    void getAllAudits() throws Exception {
        List<Audit> audits = List.of(createAudit(1L, "Audit 1"));
        when(auditRepository.findAll()).thenReturn(audits);

        List<?> result = controlService.getAllControlsByType("audit");
        assertEquals(audits, result);
        verify(auditRepository, times(1)).findAll();
    }

    @Test
    void getAllLegislations() throws Exception {
        List<Legislation> legislations = List.of(createLegislation(1L, "Legislation 1"));
        when(legislationRepository.findAll()).thenReturn(legislations);

        List<?> result = controlService.getAllControlsByType("legislation");
        assertEquals(legislations, result);
        verify(legislationRepository, times(1)).findAll();
    }

    @Test
    void getAllDocuments() throws Exception {
        List<Document> documents = List.of(createDocument(1L, "Document 1"));
        when(documentRepository.findAll()).thenReturn(documents);

        List<?> result = controlService.getAllControlsByType("document");
        assertEquals(documents, result);
        verify(documentRepository, times(1)).findAll();
    }

    @Test
    void getAllControlsByType_UnknownType() throws Exception {
        assertThrows(ResponseStatusException.class, () -> {
            controlService.getAllControlsByType("unknown");
        });
    }

    // --- /process endpoint tests ---

    @Test
    void getAuditsByProcessId() throws Exception {
        Long processId = 10L;
        List<Audit> audits = List.of(createAudit(1L, "Audit 1"));
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());
        when(auditRepository.findByProcessId(processId)).thenReturn(audits);

        List<?> result = controlService.getControlsByTypeAndProcessId("audit", processId);
        assertEquals(audits, result);
        verify(auditRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getLegislationsByProcessId() throws Exception {
        Long processId = 11L;
        List<Legislation> legislations = List.of(createLegislation(1L, "Legislation 1"));
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());
        when(legislationRepository.findByProcessId(processId)).thenReturn(legislations);

        List<?> result = controlService.getControlsByTypeAndProcessId("legislation", processId);
        assertEquals(legislations, result);
        verify(legislationRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getDocumentsByProcessId() throws Exception {
        Long processId = 12L;
        List<Document> documents = List.of(createDocument(1L, "Document 1"));
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());
        when(documentRepository.findByProcessId(processId)).thenReturn(documents);

        List<?> result = controlService.getControlsByTypeAndProcessId("document", processId);
        assertEquals(documents, result);
        verify(documentRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getControlByTypeAndProcessId_ProcessNotFound() throws Exception {
        Long processId = 13L;
        when(processService.getProcessById(processId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            controlService.getControlsByTypeAndProcessId("audit", processId);
        });
    }

    @Test
    void getControlByTypeAndProcessId_UnknownType() throws Exception {
        Long processId = 14L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        assertThrows(ResponseStatusException.class, () -> {
            controlService.getControlsByTypeAndProcessId("unknown", processId);
        });
    }
}
