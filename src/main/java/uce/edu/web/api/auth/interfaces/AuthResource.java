package uce.edu.web.api.auth.interfaces;

import java.time.Instant;
import java.util.Set;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import uce.edu.web.api.auth.application.UsuarioService;
import uce.edu.web.api.auth.application.representation.UsuarioRepresentation;

@Path("/auth")
public class AuthResource {

    @Inject
    UsuarioService usuarioService;

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public TokenResponse token(
            @QueryParam("user") String user,
            @QueryParam("password") String password
    ) {

        // Donde se compara el password y usuario con la base
        // TAREA: Implementar la validación contra una base de datos
        // Crear tabla usuarios (id, user, password, role)
        // boolean ok = true;
        //String role = "admin";
        UsuarioRepresentation usuario = usuarioService.validarCredenciales(user, password);
        if (usuario != null) {
            String issuer = "matricula-auth";
            long ttl = 3600;
 
            Instant now = Instant.now();
            Instant exp = now.plusSeconds(ttl);
 
            String jwt = Jwt.issuer(issuer)
                .subject(usuario.username)
                .groups(Set.of(usuario.rol))     // roles: user / admin
                .issuedAt(now)
                .expiresAt(exp)
                .sign();
 
            return new TokenResponse(jwt, exp.getEpochSecond(), usuario.rol);
        } else {
            return null; // manejar error de autenticación
        }
       
    }  

    public static class TokenResponse {
        public String accessToken;
        public long expiresAt;
        public String role;
 
        public TokenResponse() {}
        public TokenResponse(String accessToken, long expiresAt, String role) {
            this.accessToken = accessToken;
            this.expiresAt = expiresAt;
            this.role = role;
        }
    }
}
