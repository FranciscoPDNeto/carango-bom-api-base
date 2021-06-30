package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.config.security.dtos.TokenResponse;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AutenticationController {

    private AuthenticationManager authenticationManager;

    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> autenticate(@RequestBody @Valid UsuarioRequest userRequest) {
        UsernamePasswordAuthenticationToken loginData = userRequest.converter();
        try {
            Authentication authentication = authenticationManager.authenticate(loginData);
            String token = tokenService.generateToken(authentication);
            return ResponseEntity.ok(new TokenResponse(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
