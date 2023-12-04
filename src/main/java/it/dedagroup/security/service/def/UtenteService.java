package it.dedagroup.security.service.def;

import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.List;

public interface UtenteService {
    Utente login(String email, String password);

    Utente findByEmail(String email);
    Utente findByTelefono(String telefono);
    Utente findByDataDiNascita(LocalDate dataDiNascita);
    List<Utente> findByNomeAndCognome(String nome, String cognome);
    Utente findByRuolo(Ruolo ruolo);
    Utente aggiungiUtente(Utente utente);
    Utente modificaUtente(Utente utente, long idUtente);
    Utente eliminaUtente(long id);

    Utente findByIdAndIsCancellatoFalse(long id);
    List<Utente> findAllById(List<Long>ids);
    Utente findByEmailAndPasswordAndIsCancellatoFalse(String email, String password);
}
