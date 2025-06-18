package com.backend.dashboard_tool.entity.Finance;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Product;
import com.backend.dashboard_tool.entity.Strategy.Service;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;


/**
 * This class is used to represent the revenues, which are a type of cash flow.
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
public class Revenue extends CashFlow{
    /**
	 * This field is ignored during JSON serialization to prevent circular
	 * references. This applies to all the relationships below.
	 */
    @JsonIgnore
    @ManyToMany(mappedBy = "revenues")
    private List<ProcessEntity> processes;

	@JsonIgnore
	@ManyToMany(mappedBy = "revenues")
	private List<Product> products;

    @JsonIgnore
    @ManyToMany(mappedBy = "revenues")
    private List<Service> services;
}