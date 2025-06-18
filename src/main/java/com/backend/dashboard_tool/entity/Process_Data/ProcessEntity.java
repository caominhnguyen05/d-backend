package com.backend.dashboard_tool.entity.Process_Data;

import java.util.*;

import com.backend.dashboard_tool.entity.Assets_Facilities.*;
import com.backend.dashboard_tool.entity.Finance.*;
import com.backend.dashboard_tool.entity.People.*;
import com.backend.dashboard_tool.entity.Strategy.*;
import com.backend.dashboard_tool.entity.Document;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class is used to represent the processes.
 * This forms the basis of the application. Most other classes/entities have a relation with
 * processEntity. To not confuse with java.lang.Process, we use ProcessEntity.
 * It contains fields for the process name, type, description, and
 * many relationships.
 */
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name = "process")
public class ProcessEntity {
    /**
     * The unique identifier for the process.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String soort;
    private Boolean internal;
    private Integer level;
    private String description;

    /**
     * This field is ignored during JSON serialization to prevent circular
     * references. This applies to all the relationships below.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "parentProcess")
    private List<ProcessEntity> subProcesses = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_process_id")
    private ProcessEntity parentProcess = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_problem",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "problem_id")
    )
    private List<Problem> problems = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_improvement",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "improvement_id")
    )
    private List<Improvement> improvements = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sourceProcess")
    private List<Dataflow> outgoingFlows = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "targetProcess")
    private List<Dataflow> incomingFlows = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sourceProcess")
    private List<Result> outgoingResults = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "targetProcess")
    private List<Trigger> incomingTriggers = new ArrayList<>();


    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_datastore",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "datastore_id")
    )
    private List<DataStore> dataStores = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_cost",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "cost_id")
    )
    private List<Cost> costs = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_revenue",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "revenue_id")
    )
    private List<Revenue> revenues = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_goal",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "goal_id")
    )
    private List<Goal> goals = new ArrayList<>();
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_program",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "program_id")
    )
    private List<Program> programs = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_kpi",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "kpi_id")
    )
    private List<KPI> kpis = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_risk",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "risk_id")
    )
    private List<Risk> risks = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_mitigation_measure",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "mitigation_measure_id")
    )
    private List<MitigationMeasure> mitigationMeasures = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_audit",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "audit_id")
    )
    private List<Audit> audits = new ArrayList<>();

    @JsonIgnore
    @ManyToMany	
    @JoinTable(
        name = "process_product",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_service",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_legislation",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "legislation_id")
    )
    private List<Legislation> legislations = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_project",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_system",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "system_id")
    )
    private List<SystemEntity> systems = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_asset",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "asset_id")
    )
    private List<Asset> assets = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_facility",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<Facility> facilities = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_application",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "application_id")
    )
    private List<Application> applications = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_location",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<Location> locations = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_role",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_organization",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    private List<Organization> organizations = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_department",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private List<Department> departments = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_employee",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_function",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "function_id")
    )
    private List<Function> functions = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_consultation",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "consultation_id")
    )
    private List<Consultation> consultations = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "process_document",
        joinColumns = @JoinColumn(name = "process_id"),
        inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> documents = new ArrayList<>();

    public ProcessEntity copy(){
        ProcessEntity copy = new ProcessEntity();
        copy.setId(this.id);
        copy.setName(this.name);
        copy.setType(this.type);
        copy.setSoort(this.soort);
        copy.setInternal(this.internal);
        copy.setLevel(this.level);
        copy.setDescription(this.description);
        copy.setSubProcesses(new ArrayList<>(this.subProcesses));
        copy.setParentProcess(this.parentProcess);
        copy.setProblems(new ArrayList<>(this.problems));
        copy.setImprovements(new ArrayList<>(this.improvements));
        copy.setOutgoingFlows(new ArrayList<>(this.outgoingFlows));
        copy.setIncomingFlows(new ArrayList<>(this.incomingFlows));
        copy.setOutgoingResults(new ArrayList<>(this.outgoingResults));
        copy.setIncomingTriggers(new ArrayList<>(this.incomingTriggers));
        copy.setDataStores(new ArrayList<>(this.dataStores));
        copy.setCosts(new ArrayList<>(this.costs));
        copy.setRevenues(new ArrayList<>(this.revenues));
        copy.setGoals(new ArrayList<>(this.goals));
        copy.setPrograms(new ArrayList<>(this.programs));
        copy.setKpis(new ArrayList<>(this.kpis));
        copy.setRisks(new ArrayList<>(this.risks));
        copy.setMitigationMeasures(new ArrayList<>(this.mitigationMeasures));
        copy.setAudits(new ArrayList<>(this.audits));
        copy.setProducts(new ArrayList<>(this.products));
        copy.setServices(new ArrayList<>(this.services));
        copy.setLegislations(new ArrayList<>(this.legislations));
        copy.setProjects(new ArrayList<>(this.projects));
        copy.setSystems(new ArrayList<>(this.systems));
        copy.setAssets(new ArrayList<>(this.assets));
        copy.setFacilities(new ArrayList<>(this.facilities));
        copy.setApplications(new ArrayList<>(this.applications));
        copy.setLocations(new ArrayList<>(this.locations));
        copy.setRoles(new ArrayList<>(this.roles));
        copy.setOrganizations(new ArrayList<>(this.organizations));
        copy.setDepartments(new ArrayList<>(this.departments));
        copy.setEmployees(new ArrayList<>(this.employees));
        copy.setFunctions(new ArrayList<>(this.functions));
        copy.setConsultations(new ArrayList<>(this.consultations));
        copy.setDocuments(new ArrayList<>(this.documents));

        return copy;
    }
}