package com.backend.dashboard_tool.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;


/**
 * TriggerInitializer is responsible for initializing database triggers
 * that notify the application of changes to specific tables.
 * This class implements ApplicationRunner to ensure
 * the triggers are set up when the application starts.
 * It creates a trigger function that sends notifications
 * to the 'cache_update' channel whenever
 * an INSERT, UPDATE, or DELETE operation occurs on the specified tables.
 */
@Component
public class TriggerInitializer implements ApplicationRunner {


    @Autowired
    private DataSource dataSource; // Injected DataSource to obtain database connections

    private List<String> layerEntityTables = List.of(
    "audit", "legislation", "document",
    "data_store", "application", "system", "asset", "facility",
    "location", "improvement", "project", "department", "employee",
    "function", "organization"
    );

    private List<String> processJointables = List.of(
        "process_audit", "process_legislation", "process_document", "process_datastore", "process_application",
        "process_system", "process_asset", "process_facility", "process_location", "process_improvement",
        "process_project", "process_department", "process_employee", "process_function", "process_organization"
    );

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("TriggerInitializer: Successfully obtained Connection from DataSource:" + connection);

            try (Statement stmt = connection.createStatement()) {
                System.out.println("TriggerInitializer: Starting database trigger initialization...");
                
                // Generates a generic trigger function that notifies the application on changes
                // specifying the table name, operation type, and ID of the affected row.
                stmt.execute("""
                    DO $$
                    BEGIN
                        IF NOT EXISTS (
                            SELECT 1 FROM pg_proc WHERE proname = 'notify_cache_update_trigger_function'
                        ) THEN
                            CREATE OR REPLACE FUNCTION notify_cache_update_trigger_function()
                            RETURNS TRIGGER AS $func$
                            DECLARE
                              payload TEXT;
                              id_val TEXT;
                            BEGIN
                              IF (TG_OP = 'DELETE') THEN
                                id_val := OLD.id::TEXT;
                              ELSE
                                id_val := NEW.id::TEXT;
                              END IF;
                              payload := TG_TABLE_NAME || ':' || TG_OP || ':' || id_val;
                              PERFORM pg_notify('cache_update', payload);
                              RETURN NULL;
                            END;
                            $func$ LANGUAGE plpgsql;
                        END IF;
                    END;
                    $$;
                """);
                System.out.println("TriggerInitializer: Created or ensured 'notify_cache_update_trigger_function'.");

                // Generates a generic trigger function that notifies the application on changes for join tables
                // specifying the table name and operation type.
                stmt.execute("""
                    DO $$
                    BEGIN
                        IF NOT EXISTS (
                            SELECT 1 FROM pg_proc WHERE proname = 'notify_jointable_cache_update_trigger_function'
                        ) THEN
                            CREATE OR REPLACE FUNCTION notify_jointable_cache_update_trigger_function()
                            RETURNS TRIGGER AS $func$
                            BEGIN
                            PERFORM pg_notify('cache_update', TG_TABLE_NAME || ':' || TG_OP);
                            RETURN NULL;
                            END;
                            $func$ LANGUAGE plpgsql;
                        END IF;
                    END;
                    $$;
                """);
                System.out.println("TriggerInitializer: Created or ensured 'notify_jointable_cache_update_trigger_function'.");
                
                // Ensure the trigger exists for the 'process' table
                stmt.execute("""
                    DO $$
                    BEGIN
                        IF NOT EXISTS (
                            SELECT 1 FROM pg_trigger
                            WHERE tgname = 'process_cache_update_trigger' AND tgrelid = 'process'::regclass
                        ) THEN
                            CREATE TRIGGER process_cache_update_trigger
                            AFTER INSERT OR UPDATE OR DELETE ON process
                            FOR EACH ROW
                            EXECUTE FUNCTION notify_cache_update_trigger_function();
                        END IF;
                    END;
                    $$;
                """);
                System.out.println("TriggerInitializer: Ensured 'process_cache_update_trigger' on 'process' table exists.");
                
                // Ensure the trigger exists for the 'information_flow' table
                stmt.execute("""
                    DO $$
                    BEGIN
                        IF NOT EXISTS (
                            SELECT 1 FROM pg_trigger
                            WHERE tgname = 'informationflow_cache_update_trigger' AND tgrelid = 'information_flow'::regclass
                        ) THEN
                            CREATE TRIGGER informationflow_cache_update_trigger
                            AFTER INSERT OR UPDATE OR DELETE ON information_flow
                            FOR EACH ROW
                            EXECUTE FUNCTION notify_cache_update_trigger_function();
                        END IF;
                    END;
                    $$;
                """);
                System.out.println("TriggerInitializer: Ensured 'informationflow_cache_update_trigger' on 'information_flow' table exists.");

                // - Layer entity triggers -
                for (String table : layerEntityTables) {
                    String triggerName = table + "_cache_update_trigger";
                    stmt.execute(String.format("""
                        DO $$
                        BEGIN
                            IF NOT EXISTS (
                                SELECT 1 FROM pg_trigger
                                WHERE tgname = '%s' AND tgrelid = '%s'::regclass
                            ) THEN
                                CREATE TRIGGER %s
                                AFTER INSERT OR UPDATE OR DELETE ON %s
                                FOR EACH ROW
                                EXECUTE FUNCTION notify_cache_update_trigger_function();
                            END IF;
                        END;
                        $$;
                    """, triggerName, table, triggerName, table));
                    System.out.println("TriggerInitializer: Ensured '" + triggerName + "' on '" + table + "' table exists.");
                }
                // - JOIN TABLE triggers -
                for (String table : processJointables) {
                    String triggerName = table + "_cache_update_trigger";
                    stmt.execute(String.format("""
                        DO $$
                        BEGIN
                            IF NOT EXISTS (
                                SELECT 1 FROM pg_trigger
                                WHERE tgname = '%s' AND tgrelid = '%s'::regclass
                            ) THEN
                                CREATE TRIGGER %s
                                AFTER INSERT OR UPDATE OR DELETE ON %s
                                FOR EACH ROW
                                EXECUTE FUNCTION notify_jointable_cache_update_trigger_function();
                            END IF;
                        END;
                        $$;
                    """, triggerName, table, triggerName, table));
                    System.out.println("TriggerInitializer: Ensured '" + triggerName + "' on '" + table + "' join table exists.");
                }
            } 
        } catch (SQLException e) {
            System.err.println("TriggerInitializer: SQLException during database trigger initialization.");
            throw new RuntimeException("Failed to initialize database triggers due to SQLException", e);
        } catch (Exception e) {
            System.err.println("TriggerInitializer: Unexpected exception during database trigger initialization.");
            throw new RuntimeException("Failed to initialize database triggers due to unexpected exception", e);
        }
        System.out.println("TriggerInitializer: Database trigger initialization completed successfully.");
    }
}
