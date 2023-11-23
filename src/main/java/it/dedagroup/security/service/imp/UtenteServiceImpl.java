package it.dedagroup.security.service.imp;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import it.dedagroup.security.repository.UtenteRepository;
import it.dedagroup.security.service.def.UtenteService;
import jakarta.transaction.Transactional;

@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private UtenteRepository repo;

    @Override
    public Utente login(String email, String password){
        return repo.findByEmailAndPasswordAndIsCancellatoFalse(email.trim(), password.trim()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Nessun utente con queste credenziali."));
    }

    @Override
    public Utente findByEmail(String email) {
        return repo.findByEmailAndIsCancellatoFalse(email.trim()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun utente con email " + email));
    }

    @Override
    public Utente findByTelefono(String telefono) {
        return repo.findByTelefonoAndIsCancellatoFalse(telefono.trim()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Utente findByDataDiNascita(LocalDate dataDiNascita) {
        return repo.findByDataDiNascitaAndIsCancellatoFalse(dataDiNascita).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Utente findByNomeAndCognome(String nome, String cognome) {
        return repo.findByNomeAndCognomeAndIsCancellatoFalse(nome.trim(), cognome.trim()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Utente findByRuolo(Ruolo ruolo) {
        return repo.findByRuoloAndIsCancellatoFalse(ruolo).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
	@Transactional(rollbackOn = DataAccessException.class)
	public Utente aggiungiUtente(Utente utente){
		Utente utenteByEmail = repo.findByEmailAndIsCancellatoFalse(utente.getEmail()).orElse(null);
		if(utenteByEmail != null){
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente con questa email già esistente");
		}
		Utente utenteByTelefono = repo.findByTelefonoAndIsCancellatoFalse(utente.getTelefono()).orElse(null);
		if(utenteByTelefono != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente con questo numero di telefono già esistente");
		}
		if(utente.getRuolo()== Ruolo.ADMIN || utente.getRuolo()== Ruolo.SUPER_ADMIN) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Impossibile creare utente con ruolo Admin o SuperAdmin");
		}
		repo.save(utente);
		return utente;
		
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

    @Override
    public Utente findById(long id) {
        return repo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun utente con id " + id));
    }

    @Override
    public List<Utente> findAllById(List<Long> ids) {
        return repo.findAllById(ids);
    }

    @Override
    public Utente findByEmailAndPasswordAndIsCancellatoFalse(String email, String password) {
        return repo.findByEmailAndPasswordAndIsCancellatoFalse(email.trim(), password.trim()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun utente con email " + email + " e password " + password));
    }


}
