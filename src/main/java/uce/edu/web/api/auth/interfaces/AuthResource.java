package uce.edu.web.api.auth.interfaces;

import java.time.Instant;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uce.edu.web.api.auth.application.UsuarioService;
import uce.edu.web.api.auth.application.representation.UsuarioRepresentation;

@Path("/auth")
public class AuthResource {

    @Inject
    UsuarioService usuarioService;

    @ConfigProperty(name = "auth.issuer")
    String issuer;

    @ConfigProperty(name = "auth.token.ttl")
    Long ttl;

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(
            @QueryParam("user") String user,
            @QueryParam("password") String password
    ) {
        UsuarioRepresentation usuario = usuarioService.validarCredenciales(user, password);
        if (usuario != null) {
            
            Instant now = Instant.now();
            Instant exp = now.plusSeconds(ttl);
 
            String jwt = Jwt.issuer(issuer)
                .subject(usuario.username)
                .groups(Set.of(usuario.rol)) 
                .issuedAt(now)
                .expiresAt(exp)
                .sign();
 
            TokenResponse tokenResponse = new TokenResponse(jwt, exp.getEpochSecond(), usuario.rol);
            return Response.ok(tokenResponse).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();        
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
