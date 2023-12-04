package it.dedagroup.security.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTORequest {

    @NotBlank(message="Il campo username non può essere vuoto")
    @Email
    private String email;
    @NotBlank(message="Il campo password non può essere vuoto")
    private String password;


}
