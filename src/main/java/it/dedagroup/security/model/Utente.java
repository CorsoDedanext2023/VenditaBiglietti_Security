package it.dedagroup.security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private long id;
	@Column(nullable = false)
	@NotBlank
	private String nome;
	@Column(nullable = false)
	@NotBlank
	private String cognome;
	@Column(nullable = false)
	@NotNull
	private LocalDate dataDiNascita;
	@Column(nullable = false)
	@NonNull
	private Ruolo ruolo;
	@Column(nullable = false , unique = true)
	@NotBlank
	private String email;
	@Column(nullable = false)
	@NotBlank
	private String password;
	@Column(nullable = false, unique = true)
	@NotBlank
	private String telefono;
	@Column(nullable = false)
	private boolean isCancellato;
	@Version
	@Column(nullable = false, columnDefinition = "BIGINT DEFAULT 1")
	private long version;
}
