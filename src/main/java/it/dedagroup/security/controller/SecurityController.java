package it.dedagroup.security.controller;

import it.dedagroup.security.dto.LoginDTORequest;
import it.dedagroup.security.model.Utente;
import it.dedagroup.security.service.def.GestoreTokenService;
import it.dedagroup.security.service.def.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @Autowired
    UtenteService service;
    @Autowired
    GestoreTokenService gestoreTokenService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTORequest request){
        Utente u=service.login(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(gestoreTokenService.generaToken(u));
    }

    @PostMapping("/find/{token}")
    public ResponseEntity<Utente> findByToken(@PathVariable String token){
        Utente u=gestoreTokenService.getUtente(token);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

    @PostMapping("/email/{email}")
    public ResponseEntity<Utente> findByEmail(@PathVariable String email){
        Utente u=service.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }
}
