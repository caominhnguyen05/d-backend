package com.backend.dashboard_tool.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

/**
 * DatabaseNotificationListener listens for database notifications
 * on the 'cache_update' channel and processes them
 * to manage cache invalidation for specific entities.
 */
@Service
public class DatabaseNotificationListener {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DataSourceProperties dbProps; 

    /**
     * Initializes the listener for database notifications.
     * This method starts a new thread that listens for notifications
     * and processes them to clear or evict caches as needed.
     */
    @PostConstruct
    public void listenForDatabaseChanges() {
        new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(dbProps.getUrl(), dbProps.getUsername(), dbProps.getPassword())) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("LISTEN cache_update"); 
                }

                PGConnection pgConn = conn.unwrap(PGConnection.class);

                while (true) {
                    PGNotification[] notifications = pgConn.getNotifications();
                    if (notifications != null) {
                        for (PGNotification notification : notifications) {
                            System.out.println("Received raw notification: " + notification.getParameter());
                            handleNotification(notification.getParameter());
                        }
                    }

                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.err.println("DatabaseNotificationListener thread interrupted.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Error in DatabaseNotificationListener: " + e.getMessage());
                e.printStackTrace();
            }
        }, "db-notification-listener").start();
    }

    /**
     * Handles the notification payload received from the database.
     * The payload format is expected to be "tableName:operation:entityId".
     *
     * @param payload the notification payload
     */
    void handleNotification(String payload) {
        if (payload == null || payload.isEmpty()) {
            System.err.println("Received empty notification payload.");
            return;
        }
        String[] parts = payload.split(":");
        if (parts.length < 2) {
            System.err.println("Invalid notification payload format: " + payload + ". Expected tableName:OPERATION[:entityId]");
            return;
        }

        String tableName = parts[0].toLowerCase().trim();
        String operation = parts[1].toUpperCase().trim();
        Long entityId = null;

        if (parts.length > 2 && parts[2] != null && !parts[2].trim().equalsIgnoreCase("NULL") && !parts[2].trim().isEmpty()) {
            try {
                entityId = Long.parseLong(parts[2].trim());
            } catch (NumberFormatException e) {
                System.err.println("Could not parse entity ID from payload: " + payload);
            }
        }

        System.out.println("Processing notification for Table: " + tableName + ", Operation: " + operation + (entityId != null ? ", ID: " + entityId : ""));

        switch (tableName) {
            case "process":
                handleProcessEntityChange(operation, entityId);
                break;
            case "information_flow":
                handleInformationflowChange(operation, entityId);
                break;
            case "audit", "legislation", "document", "process_audit", "process_legislation", "process_document":
                handleControlServiceChange(operation);
                break;
            case "data_store", "application", "system", "process_datastore", "process_application", "process_system":
                handleIctServiceChange(operation);
                break;
            case "asset", "facility", "location", "process_asset", "process_facility", "process_location":
                handleInfrastructureServiceChange(operation);
                break;
            case "improvement", "project", "process_improvement", "process_project":
                handleInitiativeServiceChange(operation);
                break;
            case "department", "employee", "function", "organization", "role", "consultation", 
                 "process_department", "process_employee", "process_function", "process_organization", "process_role", "process_consultation":
                handlePeopleServiceChange(operation);
                break;
            case "kpi", "risk", "mitigation_measure", "process_kpi", "process_risk", "process_mitigation_measure":
                handlePerformanceServiceChange(operation);
                break;
            case "cost", "revenue", "product", "service", "goal", 
                 "process_cost", "process_revenue", "process_product", "process_service", "process_goal":
                handleRelevanceServiceChange(operation);
                break;
            default:
                System.out.println("No specific cache handling defined for table: " + tableName);
        }
    }

    /**
     * Handles changes to the Process entity, clearing relevant caches.
     *
     * @param operation the type of operation (INSERT, UPDATE, DELETE)
     * @param entityId the ID of the affected Process entity (optional)
     */
    void handleProcessEntityChange(String operation, Long entityId) {
        if (entityId != null && (operation.equals("UPDATE") || operation.equals("DELETE"))) {
            Cache processCache = cacheManager.getCache("process");
            if (processCache != null) {
                processCache.evict(entityId);
                System.out.println("Evicted from 'process' cache, ID: " + entityId);
            }
        }

        Cache processesCache = cacheManager.getCache("processes");
        if (processesCache != null) {
            processesCache.clear();
            System.out.println("Cleared 'processes' cache due to 'process' table change (" + operation + ").");
        }
    }

    /**
     * Handles changes to the Informationflow entity, clearing relevant caches.
     *
     * @param operation the type of operation (INSERT, UPDATE, DELETE)
     * @param entityId the ID of the affected Informationflow entity (optional)
     */
    void handleInformationflowChange(String operation, Long entityId) {
        Cache dataflowsCache = cacheManager.getCache("dataflows");
        if (dataflowsCache != null) {
            dataflowsCache.clear();
            System.out.println("Cleared 'dataflows' cache due to Dataflow change (" + operation + ").");
        }

        Cache inputDataflowsCache = cacheManager.getCache("inputDataflows");
        if (inputDataflowsCache != null) {
            inputDataflowsCache.clear();
            System.out.println("Cleared 'inputDataflows' cache due to Dataflow change (" + operation + ").");
        }

        Cache outputDataflowsCache = cacheManager.getCache("outputDataflows");
        if (outputDataflowsCache != null) {
            outputDataflowsCache.clear();
            System.out.println("Cleared 'outputDataflows' cache due to Dataflow change (" + operation + ").");
        }

        Cache resultCache = cacheManager.getCache("results");
        if (resultCache != null) {
            resultCache.clear();
            System.out.println("Cleared 'results' cache due to Result change (" + operation + ").");
        }

        Cache triggerCache = cacheManager.getCache("triggers");
        if (triggerCache != null) {
            triggerCache.clear();
            System.out.println("Cleared 'triggers' cache due to Trigger change (" + operation + ").");
        }
    }

    /**
     * Handles changes to ControlService entities like Audit, Legislation, and Document
     * @param operation the type of operation (INSERT, UPDATE, DELETE)
     */
    void handleControlServiceChange(String operation) {
        Cache controlAllCache = cacheManager.getCache("controls");
        if (controlAllCache != null) {
            controlAllCache.clear();
            System.out.println("Cleared 'controls' cache due to ControlService change (" + operation + ").");
        }

        Cache controlByProcessCache = cacheManager.getCache("controlByProcess");
        if (controlByProcessCache != null) {
            controlByProcessCache.clear();
            System.out.println("Cleared 'controlByProcess' cache due to ControlService change (" + operation + ").");
        }
    }


    /**
     * Handles changes to ICT entities like DataStore, Application, and System
     * @param operation the type of operation (INSERT, UPDATE, DELETE)
     */
    void handleIctServiceChange(String operation) {
        Cache ictAllCache = cacheManager.getCache("icts");
        if (ictAllCache != null) {
            ictAllCache.clear();
            System.out.println("Cleared 'icts' cache due to ICT change (" + operation + ").");
        }

        Cache ictByProcessCache = cacheManager.getCache("ictByProcess");
        if (ictByProcessCache != null) {
            ictByProcessCache.clear();
            System.out.println("Cleared 'ictByProcess' cache due to ICT change (" + operation + ").");
        }
    }

    /**
     * Handles changes to Infrastructure entities like Asset, Facility, and Location
     * @param operation the type of operation (INSERT, UPDATE, DELETE)
     */
    void handleInfrastructureServiceChange(String operation) {
        Cache infrastructureAllCache = cacheManager.getCache("infrastructures");
        if (infrastructureAllCache != null) {
            infrastructureAllCache.clear();
            System.out.println("Cleared 'infrastructures' cache due to Infrastructure change (" + operation + ").");
        }

        Cache infrastructureByProcessCache = cacheManager.getCache("infrastructureByProcess");
        if (infrastructureByProcessCache != null) {
            infrastructureByProcessCache.clear();
            System.out.println("Cleared 'infrastructureByProcess' cache due to Infrastructure change (" + operation + ").");
        }
    }

    /**
     * Handles changes to Initiative entities like Improvement and Project
     * @param operation the type of operation (INSERT, UPDATE, DELETE)
     */
    void handleInitiativeServiceChange(String operation) {
        Cache initiativeAllCache = cacheManager.getCache("initiatives");
        if (initiativeAllCache != null) {
            initiativeAllCache.clear();
            System.out.println("Cleared 'initiatives' cache due to Initiative change (" + operation + ").");
        }

        Cache initiativeByProcessCache = cacheManager.getCache("initiativeByProcess");
        if (initiativeByProcessCache != null) {
            initiativeByProcessCache.clear();
            System.out.println("Cleared 'initiativeByProcess' cache due to Initiative change (" + operation + ").");
        }
    }

    /**
     * Handles changes to People entities like Department, Employee, Function, Organization, Role, and Consultation
     * @param operation the type of operation (INSERT, UPDATE, DELETE)
     */
    void handlePeopleServiceChange(String operation) {
        Cache peopleAllCache = cacheManager.getCache("people");
        if (peopleAllCache != null) {
            peopleAllCache.clear();
            System.out.println("Cleared 'people' cache due to People change (" + operation + ").");
        }

        Cache peopleByProcessCache = cacheManager.getCache("peopleByProcess");
        if (peopleByProcessCache != null) {
            peopleByProcessCache.clear();
            System.out.println("Cleared 'peopleByProcess' cache due to People change (" + operation + ").");
        }
    }

    /**
     * Handles changes to Performance entities like KPI, Risk, and Mitigation Measure
     * @param operation the type of operation (INSERT, UPDATE, DELETE)
     */
    void handlePerformanceServiceChange(String operation) {
        Cache performanceAllCache = cacheManager.getCache("performances");
        if (performanceAllCache != null) {
            performanceAllCache.clear();
            System.out.println("Cleared 'performances' cache due to Performance change (" + operation + ").");
        }

        Cache performanceByProcessCache = cacheManager.getCache("performanceByProcess");
        if (performanceByProcessCache != null) {
            performanceByProcessCache.clear();
            System.out.println("Cleared 'performanceByProcess' cache due to Performance change (" + operation + ").");
        }
    }

    /**
     * Handles changes to Relevance entities like Cost, Revenue, Product, Service, and Goal
     * @param operation the type of operation (INSERT, UPDATE, DELETE)
     */
    void handleRelevanceServiceChange(String operation) {
        Cache relevanceAllCache = cacheManager.getCache("relevances");
        if (relevanceAllCache != null) {
            relevanceAllCache.clear();
            System.out.println("Cleared 'relevances' cache due to Relevance change (" + operation + ").");
        }

        Cache relevanceByProcessCache = cacheManager.getCache("relevanceByProcess");
        if (relevanceByProcessCache != null) {
            relevanceByProcessCache.clear();
            System.out.println("Cleared 'relevanceByProcess' cache due to Relevance change (" + operation + ").");
        }
    }
}
