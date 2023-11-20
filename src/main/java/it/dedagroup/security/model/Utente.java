package it.dedagroup.security.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Utente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private long id;
	@Column(nullable = false)
	@NonNull
	private String nome;
	@Column(nullable = false)
	@NonNull
	private String cognome;
	@Column(nullable = false)
	@NonNull
	private LocalDate dataDiNascita;
	@Column(nullable = false)
	@NonNull
	private Ruolo ruolo;
	@Column(nullable = false , unique = true)
	@NonNull
	private String email;
	@Column(nullable = false)
	@NonNull
	private String password;
	@Column(nullable = false, unique = true)
	@NonNull
	private String telefono;
	@Column(nullable = false)
	private boolean isCancellato;
	@Version
	@Column(nullable = false, columnDefinition = "BIGINT DEFAULT 1")
	private long version;
}
