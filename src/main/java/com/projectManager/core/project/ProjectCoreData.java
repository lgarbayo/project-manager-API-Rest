package com.projectManager.core.project;

import com.projectManager.core.dateType.DateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCoreData {
    private String uuid;
    @NonNull private String title;
    private String description;
    @NonNull private DateType startDate;
    @NonNull private DateType endDate;
    private Map<String, String> additionalFields;
}