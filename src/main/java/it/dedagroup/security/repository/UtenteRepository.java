package it.dedagroup.security.repository;

import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface UtenteRepository extends JpaRepository<Utente, Long>{
	
	public Optional<Utente> findByEmailAndPasswordAndIsCancellatoFalse(String email, String password);
	public Optional<Utente> findByTelefonoAndIsCancellatoFalse(String telefono);
	public Optional<Utente> findByDataDiNascitaAndIsCancellatoFalse(LocalDate dataDiNascita);
	public Optional<Utente> findByNomeAndCognomeAndIsCancellatoFalse(String nome, String cognome);
	public Optional<Utente> findByRuoloAndIsCancellatoFalse(Ruolo ruolo);
	public Optional<Utente> findByEmailAndIsCancellatoFalse(String email);

}
