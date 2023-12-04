package it.dedagroup.security.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ArgumentDTOResponse {
    private List<String> codes;
    private Object arguments;
    private String defaultMessage;
    private String code;
}
