package com.example.lojadecarros.security;

import com.example.lojadecarros.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {


    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    private static final long EXPIRATION_TIME = 86400000;


    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("role", usuario.getRole().name()) // Guarda se é gerente, vendedor, etc.
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }


    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}