package com.project_manager.shared.exception;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String type;
    private String description;
    private String path;
    private LocalDateTime timestamp;
}
