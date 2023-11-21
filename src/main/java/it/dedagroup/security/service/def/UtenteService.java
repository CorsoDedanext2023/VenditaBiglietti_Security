package it.dedagroup.security.service.def;

import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;

public interface UtenteService {
    Utente login(String email, String password);

    Utente findByEmail(String email);
    Utente findByTelefono(String telefono);
    Utente findByDataDiNascita(LocalDate dataDiNascita);
    Utente findByNomeAndCognome(String nome, String cognome);
    Utente findByRuolo(Ruolo ruolo);
    void aggiungiUtente(Utente utente);
    Utente modificaUtente(Utente utente);
    Utente eliminaUtente(long id);
}
