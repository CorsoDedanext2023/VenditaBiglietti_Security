package it.dedagroup.security.repository;

import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface UtenteRepository extends JpaRepository<Utente, Long>{
	
	public Optional<Utente> findByEmailAndPassword(String email, String password);
	public Optional<Utente> findByTelefono(String telefono);
	public Optional<Utente> findByDataDiNascita(LocalDate dataDiNascita);
	public Optional<Utente> findByNomeAndCognome(String nome, String cognome);
	public Optional<Utente> findByRuolo(Ruolo ruolo);
	public Optional<Utente> findByNomeAndCognomeAndIsCancellatoTrue(String nome, String cognome);
	public Optional<Utente> findByEmail(String email);

}
