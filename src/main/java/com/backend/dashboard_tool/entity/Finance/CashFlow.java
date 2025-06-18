package com.backend.dashboard_tool.entity.Finance;


import jakarta.persistence.*;
import lombok.*;


/**
 * This class is used to represent the cash flows that flow accross the organization.
 * It contains fields for the cash flow name, type, description, amount, and
 * relationships with other entities such as roles, organizations, departments, and many more.
 * Cash flows represent a key aspect of financial management, tracking the inflow and outflow of cash.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class CashFlow {
	/**
	 * The unique identifier for the cost.
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
    private double amount;
    private double bruto;
    private double netto;
}
