package com.projectManager.adapter.controller.response;

import com.projectManager.domain.dateType.DateType;
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