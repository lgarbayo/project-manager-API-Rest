package com.projectManager.core.dateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateType {
    private int year;
    private int month; // 0-11
    private int week;  // 0-3
}