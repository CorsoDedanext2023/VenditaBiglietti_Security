package it.dedagroup.security.service.def;

import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;

import java.time.LocalDateTime;

public interface GestoreTokenService {
    String generaToken(Utente u);

    String getUsername(String token);

    LocalDateTime getExpirationTime(String token);

    String getTelefono(String token);

    Ruolo getRuolo(String token);

    Utente getUtente(String token);
}
