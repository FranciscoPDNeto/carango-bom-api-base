package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.basecrud.BaseCrudController;
import br.com.caelum.carangobom.basecrud.BaseCrudService;
import br.com.caelum.carangobom.config.security.TokenService;
import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.dtos.PasswordRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController extends BaseCrudController<UsuarioResponse, UsuarioRequest> {

    private UsuarioService usuarioService;

    @Override
    public BaseCrudService<UsuarioResponse, UsuarioRequest> getService() {
        return usuarioService;
    }

    @Override
    public URI getPathFromResponse(UsuarioResponse response, UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(response.getId()).toUri();
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
