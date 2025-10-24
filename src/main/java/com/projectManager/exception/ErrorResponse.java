package com.projectManager.exception;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @NonNull private int status;
    @NonNull private String type;
    private String description;
    private String path;
    private LocalDateTime timestamp;
}
