package com.backend.dashboard_tool.DTO;

import jakarta.validation.constraints.NotNull;

/**
 * ProcessDTO is a Data Transfer Object that represents a process
 * in the system.
 * It contains information about the process such as its name, type,
 * soort, whether it is internal, a description,
 * and the ID of its parent process.
 */
public record ProcessDTO(@NotNull String name, 
                        @NotNull String type, 
                        String soort, 
                        Boolean internal, 
                        String description, 
                        Long parent) {
}
