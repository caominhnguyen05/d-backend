package com.backend.dashboard_tool.DTO;

import jakarta.validation.constraints.NotNull;

/**
 * ResultDTO is a Data Transfer Object that represents a result
 * between two processes in the system.
 * It contains information about the result such as its name, soort,
 * source process and target organization IDs, and other relevant details.
 */
public record ResultDTO(@NotNull String name,  
                          String soort, 
                          Boolean internal,  
                          String description,
                          @NotNull Long source,
                          @NotNull Long target) {
}
