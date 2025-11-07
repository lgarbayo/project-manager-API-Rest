package com.projectManager.adapter.controller.command;

import com.projectManager.core.dateType.DateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertTaskCommand {
    @NonNull private String title;
    private String description;
    private int durationWeeks;
    @NonNull private DateType startDate;
}
