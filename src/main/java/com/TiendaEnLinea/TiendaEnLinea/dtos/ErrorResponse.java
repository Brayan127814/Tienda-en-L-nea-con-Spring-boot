package com.TiendaEnLinea.TiendaEnLinea.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String message;
    private int status;
    private LocalDateTime timesMap;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
