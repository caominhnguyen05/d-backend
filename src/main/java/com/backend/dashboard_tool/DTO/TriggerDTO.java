package com.backend.dashboard_tool.DTO;

import jakarta.validation.constraints.NotNull;

/**
 * TriggerDTO is a Data Transfer Object that represents a trigger
 * between two processes in the system.
 * It contains information about the trigger such as its name, soort,
 * source organization and target process IDs, and other relevant details.
 */
public record TriggerDTO(@NotNull String name,  
                          String soort, 
                          Boolean internal,  
                          String description,
                          @NotNull Long source,
                          @NotNull Long target) {
}
