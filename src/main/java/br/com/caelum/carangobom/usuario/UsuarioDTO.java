package br.com.caelum.carangobom.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String password;

    public Usuario convert() {
        Usuario usuario = new Usuario();
        usuario.setName(name);
        usuario.setPassword(password);
        return usuario;
    }
}
