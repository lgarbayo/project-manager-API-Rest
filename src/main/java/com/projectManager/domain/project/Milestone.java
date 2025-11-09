package com.projectManager.domain.project;

import com.projectManager.core.dateType.DateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Milestone {
    private String uuid;
    private String projectUuid;
    @NonNull private String title;
    @NonNull private DateType date;
    private String description;
}