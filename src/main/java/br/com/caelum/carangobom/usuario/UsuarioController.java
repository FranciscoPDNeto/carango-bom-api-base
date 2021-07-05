package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.config.security.TokenService;
import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.dtos.PasswordRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("usuarios")
@Transactional
@AllArgsConstructor
public class UsuarioController {

    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> save(@Valid @RequestBody UsuarioRequest usuarioRequest, UriComponentsBuilder uriBuilder) {
        try {
            var usuarioResponse = usuarioService.registerNewUser(usuarioRequest);
            var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioResponse.getId()).toUri();
            return ResponseEntity.created(uri).body(usuarioResponse);
        } catch (UsuarioAlreadyRegisteredException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Validated @PathVariable Long id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<Void> updatePassword(
        @Valid @RequestBody PasswordRequest password, 
        @RequestHeader (name="Authorization") String token
    ) {
        try {
            usuarioService.updatePassword(password.getPassword(), TokenService.retrieveTokenFromHeaderValue(token));
            return ResponseEntity.noContent().build();
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
