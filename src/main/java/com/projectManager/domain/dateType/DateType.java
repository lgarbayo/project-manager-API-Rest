package com.projectManager.domain.dateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateType {
    @NonNull private int year;
    @NonNull private int month;
    @NonNull private int day;
}