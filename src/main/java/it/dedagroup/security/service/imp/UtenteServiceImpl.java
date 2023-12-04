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

    @Autowired
    MailSenderService mailSenderService;

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
    public List<Utente> findByNomeAndCognome(String nome, String cognome) {
        String nomeCap = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
        String cognomeCap = nome.substring(0, 1).toUpperCase() + cognome.substring(1).toLowerCase();
        List<Utente> utenti = repo.findByNomeAndCognomeAndIsCancellatoFalse(nomeCap.trim(), cognomeCap.trim());
        if(utenti.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun utente con questo nome e questo cognome.");
        }
        return utenti;
    }

    @Override
    public Utente findByRuolo(Ruolo ruolo) {
        return repo.findByRuoloAndIsCancellatoFalse(ruolo).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
	@Transactional(rollbackOn = DataAccessException.class)
	public Utente aggiungiUtente(Utente u){
		Utente utenteByEmail = repo.findByEmailAndIsCancellatoFalse(u.getEmail()).orElse(null);
		if(utenteByEmail != null){
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente con questa email già esistente.");
		}
		Utente utenteByTelefono = repo.findByTelefonoAndIsCancellatoFalse(u.getTelefono()).orElse(null);
		if(utenteByTelefono != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente con questo numero di telefono già esistente.");
		}
		if(u.getRuolo()== Ruolo.SUPER_ADMIN) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Impossibile creare utente con ruolo SuperAdmin.");
		}
        String nomeCap = u.getNome().substring(0, 1).toUpperCase() + u.getNome().substring(1).toLowerCase();
        String cognomeCap = u.getCognome().substring(0, 1).toUpperCase() + u.getCognome().substring(1).toLowerCase();
        u.setNome(nomeCap);
        u.setCognome(cognomeCap);
		repo.save(u);
        //potrebbe dare errore perchè outlook potrebbe bloccare l'email settata nel properties come server di invio
        mailSenderService.inviaMail(u.getEmail(), "Registrazione a ticketwo", "Benvenuto su ticketwo! La registrazione è avvenuta con successo!");
		return u;
		
	}

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public Utente modificaUtente(Utente u, long idUtente) {
        Utente utenteDaModificare = repo.findByIdAndIsCancellatoFalse(idUtente).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun utente con questo id."));
        Utente utenteByEmail = repo.findByEmailAndIsCancellatoFalse(u.getEmail()).orElse(null);
        if(utenteByEmail != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente con questa email già esistente");
        }
        Utente utenteByTelefono = repo.findByTelefonoAndIsCancellatoFalse(u.getTelefono()).orElse(null);
        if(utenteByTelefono != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente con questo numero di telefono già esistente");
        }
        if(u.getRuolo()== Ruolo.SUPER_ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Impossibile settare utente con ruolo SuperAdmin");
        }
        String nomeCap = u.getNome().substring(0, 1).toUpperCase() + u.getNome().substring(1).toLowerCase();
        String cognomeCap = u.getCognome().substring(0, 1).toUpperCase() + u.getCognome().substring(1).toLowerCase();
        utenteDaModificare.setNome(nomeCap);
        utenteDaModificare.setCognome(cognomeCap);
        utenteDaModificare.setEmail(u.getEmail());
        utenteDaModificare.setPassword(u.getPassword());
        utenteDaModificare.setTelefono(u.getTelefono());
        return repo.save(utenteDaModificare);
    }

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public Utente eliminaUtente(long id) {
        Utente u = repo.findByIdAndIsCancellatoFalse(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente con id "+ id + " non trovato"));
        if(u.getRuolo().equals(Ruolo.SUPER_ADMIN)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Non puoi disattivare un super admin.");
        }
        u.setCancellato(true);
        return repo.save(u);
    }

    @Override
    public Utente findByIdAndIsCancellatoFalse(long id) {
        return repo.findByIdAndIsCancellatoFalse(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun utente con id " + id));
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
