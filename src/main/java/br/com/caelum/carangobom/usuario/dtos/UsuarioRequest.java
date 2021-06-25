package br.com.caelum.carangobom.usuario.dtos;

import br.com.caelum.carangobom.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

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
    private String senha;

    public Usuario toModel() {
        var usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setSenha(senha);

        return usuario;
    }
}
