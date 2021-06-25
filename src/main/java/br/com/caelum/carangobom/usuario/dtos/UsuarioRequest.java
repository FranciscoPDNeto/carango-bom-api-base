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
    private String nome;
    @NotNull
    @NotEmpty
    private String senha;

    public Usuario toModel() {
        var usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSenha(senha);

        return usuario;
    }
}
