package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${carangobom.jwt.expiration}")
    private String expiration;

    @Value("${carangobom.jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        Date today = new Date();
        Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
            .setIssuer("API do CarangoBom")
            .setSubject(user.getId().toString())
            .setIssuedAt(today)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    public static String retrieveTokenFromHeaderValue(String authorizationHeaderValue) {
        if(authorizationHeaderValue == null || authorizationHeaderValue.isEmpty() || !authorizationHeaderValue.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeaderValue.split("\\s+")[1];
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.valueOf(claims.getSubject());
    }
}
