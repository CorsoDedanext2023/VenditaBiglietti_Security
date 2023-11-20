package it.dedagroup.security.service.imp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import it.dedagroup.security.repository.UtenteRepository;
import it.dedagroup.security.service.def.GestoreTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

@Service
public class GestoreTokenServiceImpl implements GestoreTokenService {

    @Autowired
    UtenteRepository uRepo;

    @Value("${it.dedagroup.jwt.key}")
    private String miaKey;

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(miaKey.getBytes());
    }

    @Override
    public String generaToken(Utente u){
        long durata = 1000L * 60 * 60 * 24 * 90;
        return Jwts.builder().claims().add("ruolo", u.getRuolo())
                .add("telefono", u.getTelefono())
                .subject(u.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + durata))
                .and().signWith(getKey()).compact();
    }

    private Claims getClaims(String token){
        JwtParser parser = Jwts.parser().verifyWith(getKey()).build();
        Jws<Claims> jws = parser.parseSignedClaims(token);
        Claims c = jws.getPayload();
        return c;
    }

    private <T> T getValue(Function<Claims, T> function, String token){
        return function.apply(getClaims(token));
    }

    @Override
    public String getUsername(String token){
        return getValue(c-> c.getSubject(), token);
    }

    public LocalDateTime getIssuedAt(String token){
        return getValue(Claims::getIssuedAt, token)
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @Override
    public LocalDateTime getExpirationTime(String token){
        return getValue(Claims::getExpiration, token)
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @Override
    public String getTelefono(String token){
        return getValue(c->c.get("telefono", String.class), token);
    }

    @Override
    public Ruolo getRuolo(String token){
        return getClaims(token).get("ruolo", Ruolo.class);
    }

    @Override
    public Utente getUtente(String token){
        return uRepo.findByEmailAndIsCancellatoFalse(getUsername(token))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
