package br.com.caelum.carangobom.usuario.dtos;

import br.com.caelum.carangobom.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UsuarioRequest {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String password;

    public Usuario toModel() {
        var usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);

        return usuario;
    }

    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
