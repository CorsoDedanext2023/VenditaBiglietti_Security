package it.dedagroup.security.controller;

import it.dedagroup.security.dto.LoginDTORequest;
import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import it.dedagroup.security.service.def.GestoreTokenService;
import it.dedagroup.security.service.def.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/utente")
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

    @PostMapping("/aggiungiUtente")
    public ResponseEntity<Void> aggiungiUtente(@RequestBody Utente u){
        String nomeCap = u.getNome().substring(0, 1).toUpperCase() + u.getNome().substring(1).toLowerCase();
        String cognomeCap = u.getCognome().substring(0, 1).toUpperCase() + u.getCognome().substring(1).toLowerCase();
        u.setNome(nomeCap);
        u.setCognome(cognomeCap);
        service.aggiungiUtente(u);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/modificaUtente")
    public ResponseEntity<Void> modificaUtente(@RequestBody Utente u){
        service.modificaUtente(u);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/eliminaUtente")
    public ResponseEntity<Void> eliminaUtente(@PathVariable long id){
        service.eliminaUtente(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
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

    @PostMapping("/trovaPerTelefono")
    public ResponseEntity<Utente> findByTelefono(@RequestParam String telefono){
        Utente u=service.findByTelefono(telefono);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

    @PostMapping("/trovaPerDataDiNascita")
    public ResponseEntity<Utente> findByDataDiNascita(@RequestParam LocalDate dataDiNascita){
        Utente u=service.findByDataDiNascita(dataDiNascita);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

    @PostMapping("/trovaPerNomeECognome")
    public ResponseEntity<Utente> findByNomeAndCognome(@RequestParam String nome, String cognome){
        Utente u=service.findByNomeAndCognome(nome, cognome);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

    @PostMapping("/trovaPerRuolo")
    public ResponseEntity<Utente> findByRuolo(@RequestParam Ruolo ruolo){
        Utente u=service.findByRuolo(ruolo);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }


}
