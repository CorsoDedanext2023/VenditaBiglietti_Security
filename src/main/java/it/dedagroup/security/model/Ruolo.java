package it.dedagroup.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Ruolo {
	
	ADMIN("ADMIN"),
	SUPER_ADMIN("SUPER_ADMIN"),
	VENDITORE("VENDITORE"),
	CLIENTE("CLIENTE");
	@Getter
	private String ruolo;
	public String toString(){return ruolo;}

}
