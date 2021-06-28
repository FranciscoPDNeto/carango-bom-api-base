package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@Transactional
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro-usuario")
    public ResponseEntity<UsuarioResponse> save(@Valid @RequestBody UsuarioRequest usuarioRequest, UriComponentsBuilder uriBuilder) {
        try {
            var usuarioResponse = usuarioService.registerNewUser(usuarioRequest);
            var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioResponse.getId()).toUri();
            return ResponseEntity.created(uri).body(usuarioResponse);
        } catch (UsuarioAlreadyRegisteredException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }
}
