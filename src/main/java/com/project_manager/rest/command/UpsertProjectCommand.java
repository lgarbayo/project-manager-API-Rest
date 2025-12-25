package com.project_manager.rest.command;

import java.util.Map;

import com.project_manager.shared.core.dateType.DateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertProjectCommand {
    @NonNull private String title;
    private String description;
    @NonNull private DateType startDate;
    @NonNull private DateType endDate;
    private Map<String, String> additionalFields;
}
