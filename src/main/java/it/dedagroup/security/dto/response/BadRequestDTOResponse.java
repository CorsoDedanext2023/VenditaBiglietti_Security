package it.dedagroup.security.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BadRequestDTOResponse {
    private LocalDateTime timestamp;
    private int status;
    private List<ErrorDTOResponse> errors;
    private String exception;
    private String message;
    private String path;
}
