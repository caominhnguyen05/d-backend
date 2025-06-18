package com.backend.dashboard_tool.DTO;

import jakarta.validation.constraints.NotNull;

/**
 * DataflowDTO is a Data Transfer Object that represents a data flow
 * between two processes in the system.
 * It contains information about the data flow such as its name, soort,
 * source and target process IDs, and other relevant details.
 */
public record DataflowDTO(@NotNull String name,  
                          String soort, 
                          Boolean internal,  
                          String description,
                          @NotNull Long source,
                          @NotNull Long target) {
}
