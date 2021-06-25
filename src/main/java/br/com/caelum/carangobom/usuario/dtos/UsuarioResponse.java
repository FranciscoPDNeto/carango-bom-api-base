package br.com.caelum.carangobom.usuario.dtos;

import br.com.caelum.carangobom.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private String username;

    public static UsuarioResponse fromModel(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getUsername()
        );
    }
}
