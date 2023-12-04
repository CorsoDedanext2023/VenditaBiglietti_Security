package it.dedagroup.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.dedagroup.security.dto.response.BadRequestDTOResponse;
import it.dedagroup.security.dto.request.LoginDTORequest;
import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import it.dedagroup.security.service.def.GestoreTokenService;
import it.dedagroup.security.service.def.UtenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/utente")
@Tag(name = "Controller che gestisce l'entità utente",
description = "Tramite gli endpoint qui contenuti si gestisce l'autenticazione degli utenti, aggiunta di e interventi su essi, " +
        "e il loro recupero tramite parametri specificati.")
public class SecurityController {

    @Autowired
    UtenteService service;
    @Autowired
    GestoreTokenService gestoreTokenService;

    @Operation(summary = "Gestione login utente.", description = "Questo endpoint permette a un utente non loggato di effettuare l'accesso." +
            "In caso di login superato correttamente, l'endpoint passa un token in formato String.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login effettuato con successo e token restituito.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Nessun utente con queste credenziali.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTORequest request){
        Utente u=service.login(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(gestoreTokenService.generaToken(u));
    }

    @Operation(summary = "Gestione aggiunta nuovo utente.", description = "Questo endpoint gestisce l'aggiunta di un nuovo utente al database, " +
            "fornendo un punto d'accesso sia all'endpoint di registrazione del main, sia ai metodi che gestiscono l'inserimento diretto di utenti venditori e admin." +
            "Non permette la creazione di un utente con ruolo superadmin." +
            "Inoltre setta l'iniziale di nome e cognome con lettera maiuscola, evitando l'inserimento di essi con iniziale minuscola." +
            "A creazione account avvenuta, invia automaticamente una mail di conferma all'utente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Utente aggiunto con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Utente con questa email già esistente, inserimento bloccato.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Utente con questo numero di telefono già esistente, inserimento bloccato.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "403", description = "Impossibile creare utente con ruolo SuperAdmin.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/aggiungiUtente")
    public ResponseEntity<Void> aggiungiUtente(@Valid @RequestBody Utente u){
        String nomeCap = u.getNome().substring(0, 1).toUpperCase() + u.getNome().substring(1).toLowerCase();
        String cognomeCap = u.getCognome().substring(0, 1).toUpperCase() + u.getCognome().substring(1).toLowerCase();
        u.setNome(nomeCap);
        u.setCognome(cognomeCap);
        service.aggiungiUtente(u);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Gestione modifica utente.", description = "Questo endpoint gestisce la modifica delle credenziali di accesso e del" +
            "numero di telefono dell'utente. Viene passato un oggetto utente e un id a lui associato e, superati i dovuti controlli riguardo l'unicità dei nuovi dati," +
            "salvato in sistema con le nuove credenziali.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Utente modificato con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Utente non trovato con questo id.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Utente con questa email già esistente, modifica bloccata.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Utente con questo numero di telefono già esistente, modifica bloccata.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "403", description = "Impossibile settare ruolo SuperAdmin all'utente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/modificaUtente/{idUtente}")
    public ResponseEntity<Void> modificaUtente(@Valid @RequestBody Utente u, @PathVariable long idUtente){
        service.modificaUtente(u, idUtente);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Gestione disattivazione utente.", description = "Questo endpoint gestisce la disattivazione di un account." +
            "La variabile isCancellato viene settata su true, rendendo non più visibile l'account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Utente disattivato con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Utente non trovato con questo id.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "403", description = "Non puoi disattivare un super admin.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),

    })
    @PostMapping("/eliminaUtente/{idUtente}")
    public ResponseEntity<Void> eliminaUtente(@PathVariable long idUtente){
        service.eliminaUtente(idUtente);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Trova utente tramite token.", description = "Questo endpoint restituisce l'utente associato al token immesso come path variable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente ritornato con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Utente.class))),
            @ApiResponse(responseCode = "404", description = "Nessun utente associato a questo token.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))

    })
    @PostMapping("/find/{token}")
    public ResponseEntity<Utente> findByToken(@PathVariable String token){
        Utente u=gestoreTokenService.getUtente(token);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

    @Operation(summary = "Trova utente tramite la sua email.", description = "Questo endpoint restituisce l'utente associato all'email immessa come path variable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente ritornato con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Utente.class))),
            @ApiResponse(responseCode = "404", description = "Nessun utente associato a questa email.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))

    })
    @PostMapping("/email/{email}")
    public ResponseEntity<Utente> findByEmail(@PathVariable String email){
        Utente u=service.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

    @Operation(summary = "Trova utente tramite il suo numero di telefono.", description = "Questo endpoint restituisce l'utente associato al numero immesso come request param.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente ritornato con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Utente.class))),
            @ApiResponse(responseCode = "404", description = "Nessun utente associato a questo numero.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))

    })
    @PostMapping("/trovaPerTelefono")
    public ResponseEntity<Utente> findByTelefono(@RequestParam String telefono){
        Utente u=service.findByTelefono(telefono);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

    @Operation(summary = "Trova utente tramite la sua data di nascita.", description = "Questo endpoint restituisce l'utente associato alla data immesso come request param.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente ritornato con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Utente.class))),
            @ApiResponse(responseCode = "404", description = "Nessun utente trovato con questa data di nascita.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))

    })
    @PostMapping("/trovaPerDataDiNascita")
    public ResponseEntity<Utente> findByDataDiNascita(@RequestParam LocalDate dataDiNascita){
        Utente u=service.findByDataDiNascita(dataDiNascita);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }


    @PostMapping("/trovaPerNomeECognome")
    public ResponseEntity<List<Utente>> findByNomeAndCognome(@RequestParam String nome, String cognome){
        return ResponseEntity.status(HttpStatus.OK).body(service.findByNomeAndCognome(nome, cognome));
    }

    @PostMapping("/trovaPerRuolo")
    public ResponseEntity<Utente> findByRuolo(@RequestParam Ruolo ruolo){
        Utente u=service.findByRuolo(ruolo);
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

    @PostMapping("/trovaPerId/{id}")
    public ResponseEntity<Utente> findById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.findByIdAndIsCancellatoFalse(id));
    }

    @PostMapping("/trovaPerListaIds")
    public ResponseEntity<List<Utente>> findAllById(@RequestBody List<Long> ids){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllById(ids));
    }

    @PostMapping("/trovaPerEmailEPassword/{email}/{password}")
    public ResponseEntity<Utente> findByEmailAndPassword(@PathVariable String email, @PathVariable String password){
        return ResponseEntity.status(HttpStatus.OK).body(service.findByEmailAndPasswordAndIsCancellatoFalse(email, password));
    }


}
