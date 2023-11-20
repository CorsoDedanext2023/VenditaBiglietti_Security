package it.dedagroup.security.service.def;

import it.dedagroup.security.model.Utente;

public interface UtenteService {
    Utente login(String email, String password);

    Utente findByEmail(String email);
}
