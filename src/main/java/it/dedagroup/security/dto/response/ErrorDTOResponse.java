package it.dedagroup.security.dto.response;

import it.dedagroup.security.dto.response.ArgumentDTOResponse;
import lombok.Data;

import java.util.List;

@Data
public class ErrorDTOResponse {
    private List<String> codes;
    private List<ArgumentDTOResponse> arguments;
    private String defaultMessage;
    private String objectName;
    private String field;
    private String rejectedValue;
    private boolean bindingFailure;
    private String code;
}
