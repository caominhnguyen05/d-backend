package com.backend.dashboard_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.Finance.CostRepository;
import com.backend.dashboard_tool.database.Finance.RevenueRepository;
import com.backend.dashboard_tool.database.Strategy.GoalRepository;
import com.backend.dashboard_tool.database.Strategy.ProductRepository;
import com.backend.dashboard_tool.database.Strategy.ServiceRepository;
import com.backend.dashboard_tool.entity.Finance.Cost;
import com.backend.dashboard_tool.entity.Finance.Revenue;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Goal;
import com.backend.dashboard_tool.entity.Strategy.Product;
import com.backend.dashboard_tool.entity.Strategy.Service;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RelevanceServiceTest {
    @Mock
    private CostRepository costRepository;

    @Mock
    private RevenueRepository revenueRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private ProcessService processService;

    @InjectMocks
    private RelevanceService relevanceService;

    /**
     * Creates a Cost object with the specified id and name.
     * @param id
     * @param name
     * @return Cost object
     */
    private Cost createCost(Long id, String name) {
        Cost cost = new Cost();
        cost.setId(id);
        cost.setName(name);
        return cost;
    }

    /**
     * Creates a Revenue object with the specified id and name.
     * @param id
     * @param name
     * @return Revenue object
     */
    private Revenue createRevenue(Long id, String name) {
        Revenue revenue = new Revenue();
        revenue.setId(id);
        revenue.setName(name);
        return revenue;
    }

    /**
     * Creates a Product object with the specified id and name.
     * @param id
     * @param name
     * @return Product object
     */
    private Product createProduct(Long id, String name) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        return product;
    }

    /**
     * Creates a Service object with the specified id and name.
     * @param id
     * @param name
     * @return Service object
     */
    private Service createService(Long id, String name) {
        Service service = new Service();
        service.setId(id);
        service.setName(name);
        return service;
    }

    /**
     * Creates a Goal object with the specified id and name.
     * @param id
     * @param name
     * @return Goal object
     */
    private Goal createGoal(Long id, String name) {
        Goal goal = new Goal();
        goal.setId(id);
        goal.setName(name);
        return goal;
    }

    // --- /all endpoint tests ---
    
    @Test
    void getAllCosts() throws Exception {
        List<Cost> costs = List.of(createCost(1L, "Cost 1"));
        when(costRepository.findAll()).thenReturn(costs);

        List<?> result = relevanceService.getAllRelevancesByType("cost");
        assertEquals(costs, result);
        verify(costRepository, times(1)).findAll();
    }

    @Test
    void getAllRevenues() throws Exception {
        List<Revenue> revenues = List.of(createRevenue(1L, "Revenue 1"));
        when(revenueRepository.findAll()).thenReturn(revenues);

        List<?> result = relevanceService.getAllRelevancesByType("revenue");
        assertEquals(revenues, result);
        verify(revenueRepository, times(1)).findAll();
    }

    @Test
    void getAllProducts() throws Exception {
        List<Product> products = List.of(createProduct(1L, "Product 1"));
        when(productRepository.findAll()).thenReturn(products);

        List<?> result = relevanceService.getAllRelevancesByType("product");
        assertEquals(products, result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getAllServices() throws Exception {
        List<Service> services = List.of(createService(1L, "Service 1"));
        when(serviceRepository.findAll()).thenReturn(services);

        List<?> result = relevanceService.getAllRelevancesByType("service");
        assertEquals(services, result);
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    void getAllGoals() throws Exception {
        List<Goal> goals = List.of(createGoal(1L, "Goal 1"));
        when(goalRepository.findAll()).thenReturn(goals);

        List<?> result = relevanceService.getAllRelevancesByType("goal");
        assertEquals(goals, result);
        verify(goalRepository, times(1)).findAll();
    }

    @Test
    void getAllRelevancesByType_UnknownType() throws Exception {
        assertThrows(ResponseStatusException.class, () -> {
            relevanceService.getAllRelevancesByType("unknown");
        });
    }

    // --- /process endpoint tests ---
    @Test
    void getCostsByProcessId() throws Exception {
        Long processId = 10L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Cost> costs = List.of(createCost(1L, "Cost 1"));
        when(costRepository.findByProcessId(processId)).thenReturn(costs);

        List<?> result = relevanceService.getRelevanceByTypeAndProcessId("cost", processId);
        assertEquals(costs, result);
        verify(costRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getRevenuesByProcessId() throws Exception {
        Long processId = 11L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Revenue> revenues = List.of(createRevenue(1L, "Revenue 1"));
        when(revenueRepository.findByProcessId(processId)).thenReturn(revenues);

        List<?> result = relevanceService.getRelevanceByTypeAndProcessId("revenue", processId);
        assertEquals(revenues, result);
        verify(revenueRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getProductsByProcessId() throws Exception {
        Long processId = 12L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Product> products = List.of(createProduct(1L, "Product 1"));
        when(productRepository.findByProcessId(processId)).thenReturn(products);

        List<?> result = relevanceService.getRelevanceByTypeAndProcessId("product", processId);
        assertEquals(products, result);
        verify(productRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getServicesByProcessId() throws Exception {
        Long processId = 13L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Service> services = List.of(createService(1L, "Service 1"));
        when(serviceRepository.findByProcessId(processId)).thenReturn(services);

        List<?> result = relevanceService.getRelevanceByTypeAndProcessId("service", processId);
        assertEquals(services, result);
        verify(serviceRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getGoalsByProcessId() throws Exception {
        Long processId = 14L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Goal> goals = List.of(createGoal(1L, "Goal 1"));
        when(goalRepository.findByProcessId(processId)).thenReturn(goals);

        List<?> result = relevanceService.getRelevanceByTypeAndProcessId("goal", processId);
        assertEquals(goals, result);
        verify(goalRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getRelevanceServicesByTypeAndProcessId_UnknownProcessId() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            relevanceService.getRelevanceByTypeAndProcessId("improvement", processId);
        });
    }

    @Test
    void getLayerByTypeAndProcessId_UnknownType() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        assertThrows(ResponseStatusException.class, () -> {
            relevanceService.getRelevanceByTypeAndProcessId("unknown", processId);
        });
    }
}
