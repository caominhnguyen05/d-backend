package com.backend.dashboard_tool.entity.People;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Process_Data.Result;
import com.backend.dashboard_tool.entity.Process_Data.Trigger;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent an Organization.
 * It contains fields for the organization name, description, and
 * relationships with other entities such as functions, departments and employees.
 */
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Organization {
    /**
     * The unique identifier for the organization.
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
    @ManyToMany(mappedBy = "organizations")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
		name = "organizations_roles",
		joinColumns = @JoinColumn(name = "organization_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
		name = "organizations_functions",
		joinColumns = @JoinColumn(name = "organization_id"),
		inverseJoinColumns = @JoinColumn(name = "function_id")
    )
    private List<Function> functions;

    @JsonIgnore
    @OneToMany(mappedBy = "organization")
    private List<Department> departments;

    @JsonIgnore
    @OneToMany(mappedBy = "organization")
    private List<Employee> employees;

    @JsonIgnore
    @OneToMany(mappedBy = "sourceOrganization")
    private List<Trigger> outgoingTriggers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "targetOrganization")
    private List<Result> incomingResults = new ArrayList<>();

    /**
     * This method is used to copy the organization object.
     * It creates a new organization object and copies the values of the fields from the current object to the new object.
     * We use it for the sipoc mapping.
     * 
     * @return a new organization object with the same values as the current object
     */
    public Organization copy() {
        Organization copy = new Organization();
        copy.setId(this.id);
        copy.setName(this.name);
        copy.setType(this.type);
        copy.setSoort(this.soort);
        copy.setInternal(this.internal);
        copy.setLevel(this.level);
        copy.setDescription(this.description);
        return copy;
    }
}
