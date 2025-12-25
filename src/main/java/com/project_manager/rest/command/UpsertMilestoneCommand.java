package com.project_manager.rest.command;

import com.project_manager.shared.core.dateType.DateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertMilestoneCommand {
    @NonNull private String title;
    @NonNull private DateType date;
    private String description;
}
