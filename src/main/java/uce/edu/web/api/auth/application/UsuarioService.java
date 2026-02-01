package uce.edu.web.api.auth.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import uce.edu.web.api.auth.application.representation.UsuarioRepresentation;
import uce.edu.web.api.auth.domain.Usuario;
import uce.edu.web.api.auth.infraestrucutre.UsuarioRepository;

@ApplicationScoped
public class UsuarioService {
   @Inject
   UsuarioRepository usuarioRepository;

   public UsuarioRepresentation validarCredenciales(String username, String password) {
      Usuario usuario = usuarioRepository.findByUsername(username);

      if (usuario != null && usuario.password.equals(password)) {
         return mapToRepresentation(usuario);
      }
      return null;
   }

   public UsuarioRepresentation mapToRepresentation(Usuario u) {
      UsuarioRepresentation dto = new UsuarioRepresentation();
      dto.username = u.username;
      dto.rol = u.rol;
      return dto;
   }
}
