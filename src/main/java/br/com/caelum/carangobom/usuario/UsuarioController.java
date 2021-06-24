package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.marca.Marca;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Transactional
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro-usuario")
    @Transactional
    public ResponseEntity<Usuario> cadastra(@Valid @RequestBody UsuarioDTO userDTO, UriComponentsBuilder uriBuilder) {
        Usuario user  = usuarioService.registerNewUser(userDTO);
        if (user == null)
            return ResponseEntity.badRequest().body(null);
        URI h = uriBuilder.path("/usuarios/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(h).body(user);
    }
}
