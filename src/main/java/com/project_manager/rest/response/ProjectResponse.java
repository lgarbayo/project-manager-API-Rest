package com.project_manager.rest.response;

import com.project_manager.shared.core.dateType.DateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private String uuid;
    private String title;
    private String description;
    private DateType startDate;
    private DateType endDate;
    private Map<String, String> additionalFields;
}