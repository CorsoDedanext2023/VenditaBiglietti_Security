package it.dedagroup.security.service.imp;

import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import it.dedagroup.security.repository.UtenteRepository;
import it.dedagroup.security.service.def.UtenteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private UtenteRepository repo;

    @Override
    public Utente login(String email, String password){
        return repo.findByEmailAndPasswordAndIsCancellatoFalse(email, password).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Nessun utente con queste credenziali."));
    }

    @Override
    public Utente findByEmail(String email) {
        return repo.findByEmailAndIsCancellatoFalse(email).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Utente findByTelefono(String telefono) {
        return repo.findByTelefonoAndIsCancellatoFalse(telefono).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Utente findByDataDiNascita(LocalDate dataDiNascita) {
        return repo.findByDataDiNascitaAndIsCancellatoFalse(dataDiNascita).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Utente findByNomeAndCognome(String nome, String cognome) {
        return repo.findByNomeAndCognomeAndIsCancellatoFalse(nome, cognome).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Utente findByRuolo(Ruolo ruolo) {
        return repo.findByRuoloAndIsCancellatoFalse(ruolo).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public void aggiungiUtente(Utente utente) {
        List<Utente> utenti = repo.findAll().stream().filter(u-> !u.isCancellato()).toList();
        for(Utente u : utenti){
            if(u.getEmail().equalsIgnoreCase(utente.getEmail())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email già presente in db.");
            }
            if(u.getTelefono().equals(utente.getTelefono())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Telefono già presente in db.");
            }
        }
        repo.save(utente);

    }

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public Utente modificaUtente(Utente utente) {
        return repo.save(utente);
    }

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public Utente eliminaUtente(long id) {
        Utente u = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente con id "+ id + " non trovato"));
        if(u.isCancellato()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Utente già cancellato.");
        u.setCancellato(true);
        return repo.save(u);
    }

}
