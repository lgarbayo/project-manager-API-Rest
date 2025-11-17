package com.project_manager.shared.core.dateType;

import org.mapstruct.Mapper;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface DateTypeMapper {

    default DateType toDateType(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        
        DateType dateType = new DateType();
        dateType.setYear(localDate.getYear());
        dateType.setMonth(localDate.getMonthValue() - 1); // LocalDate uses 1-12, DateType uses 0-11
        
        // Calculate week of month (approximate)
        int dayOfMonth = localDate.getDayOfMonth();
        int week = (dayOfMonth - 1) / 7; // 0-based week of month
        dateType.setWeek(Math.min(week, 3)); // DateType uses 0-3
        
        return dateType;
    }

    default LocalDate toLocalDate(DateType dateType) {
        if (dateType == null) {
            return null;
        }

        int month = dateType.getMonth() + 1; // DateType uses 0-11, LocalDate uses 1-12
        int dayOfMonth = (dateType.getWeek() * 7) + 1; // Approximate day from week
        
        // Ensure valid day of month
        int year = dateType.getYear();
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int maxDayOfMonth = firstDayOfMonth.lengthOfMonth();
        dayOfMonth = Math.min(dayOfMonth, maxDayOfMonth);
        
        return LocalDate.of(year, month, dayOfMonth);
    }
}
