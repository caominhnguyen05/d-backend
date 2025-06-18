package com.backend.dashboard_tool.entity.Finance;


import com.backend.dashboard_tool.entity.Assets_Facilities.Application;
import com.backend.dashboard_tool.entity.Assets_Facilities.Facility;
import com.backend.dashboard_tool.entity.Strategy.Program;
import com.backend.dashboard_tool.entity.Strategy.Project;
import com.backend.dashboard_tool.entity.Assets_Facilities.Asset;
import com.backend.dashboard_tool.entity.People.Department;
import com.backend.dashboard_tool.entity.People.Employee;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Assets_Facilities.SystemEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;


/**
 * This class is used to represent the costs, which are a type of cash flow.
 * It extends the CashFlow class and inherits its properties.
 * 
 * @see CashFlow
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
public class Cost extends CashFlow{
    /**
	 * This field is ignored during JSON serialization to prevent circular
	 * references. This applies to all the relationships below.
	 */
    @JsonIgnore
    @ManyToMany(mappedBy = "costs")
    private List<ProcessEntity> processes;

	@JsonIgnore
	@ManyToMany(mappedBy = "costs")
	private List<Application> applications;

    @JsonIgnore
    @ManyToMany(mappedBy = "costs")
    private List<SystemEntity> systems;

    @JsonIgnore
    @ManyToMany(mappedBy = "costs")
    private List<Department> departments;

    @JsonIgnore
    @ManyToMany(mappedBy = "costs")
    private List<Employee> employees;

    @JsonIgnore
    @ManyToMany(mappedBy = "costs")
    private List<Facility> facilities;

    @JsonIgnore
    @ManyToMany(mappedBy = "costs")
    private List<Project> projects;

    @JsonIgnore
    @ManyToMany(mappedBy = "costs")
    private List<Program> programs;

    @JsonIgnore
    @ManyToMany(mappedBy = "costs")
    private List<Asset> assets;
}
