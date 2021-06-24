package br.com.caelum.carangobom.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Transactional
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro-usuario")
    @Transactional
    public ResponseEntity<Usuario> cadastra(@Valid @RequestBody UsuarioDTO userDTO, UriComponentsBuilder uriBuilder) {
        var user  = usuarioService.registerNewUser(userDTO);
        if (user == null)
            return ResponseEntity.badRequest().body(null);
        var h = uriBuilder.path("/usuarios/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(h).body(user);
    }
}
