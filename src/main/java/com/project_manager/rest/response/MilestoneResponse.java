package com.project_manager.rest.response;

import com.project_manager.shared.core.dateType.DateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneResponse {
    private String uuid;
    private String projectUuid;
    private String title;
    private String description;
    private DateType date;
}