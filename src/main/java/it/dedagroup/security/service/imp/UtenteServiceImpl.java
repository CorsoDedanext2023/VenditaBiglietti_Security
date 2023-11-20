package it.dedagroup.security.service.imp;

import it.dedagroup.security.model.Utente;
import it.dedagroup.security.repository.UtenteRepository;
import it.dedagroup.security.service.def.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private UtenteRepository repo;

    @Override
    public Utente login(String email, String password){
        return repo.findByEmailAndPassword(email, password).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"nessun utetne"));
    }

    @Override
    public Utente findByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
