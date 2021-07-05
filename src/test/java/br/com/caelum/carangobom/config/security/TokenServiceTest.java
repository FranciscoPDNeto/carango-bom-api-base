package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class TokenServiceTest {

    private TokenService tokenService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void configure() {
        openMocks(this);

        tokenService = new TokenService("86400000", "batata123");
    }

    @Test
    void deveRetornarTokenGerado() {
        when(authentication.getPrincipal()).thenReturn(
            new Usuario(1L,"teste", "$2a$10$5CV4l3DqGaWx1uk83Yi1FexEEDzkuGIX76b2FRUXEZ12bIfzlLt/m"));

        var token = tokenService.generateToken(authentication);

        assertNotNull(token);
        assertTrue(!token.isEmpty());
    }

    @Test
    void deveRetornarTrueParaTokenValido() {
        when(authentication.getPrincipal()).thenReturn(
            new Usuario(1L,"teste", "$2a$10$5CV4l3DqGaWx1uk83Yi1FexEEDzkuGIX76b2FRUXEZ12bIfzlLt/m"));

        var token = tokenService.generateToken(authentication);

        assertTrue(tokenService.isValidToken(token));
    }

    @Test
    void deveRetornarFalseParaTokenInvalido() {
        var token = "invalidToken";

        assertFalse(tokenService.isValidToken(token));
    }

    @Test
    void deveRetornarIdDoUsuarioDeAcordoComToken() {
        var usuario = new Usuario(1L,"teste", "$2a$10$5CV4l3DqGaWx1uk83Yi1FexEEDzkuGIX76b2FRUXEZ12bIfzlLt/m");
        when(authentication.getPrincipal()).thenReturn(usuario);

        var token = tokenService.generateToken(authentication);

        assertEquals(usuario.getId(), tokenService.getUserId(token));
    }

    @Test
    void deveRetornarTokenComValorDoHeader() {
        var authorizationHeaderValue = "Bearer token";

        var token = TokenService.retrieveTokenFromHeaderValue(authorizationHeaderValue);

        assertEquals("token", token);
    }

    @Test
    void deveRetornarNuloComValorDoHeaderNulo() {
        assertNull(TokenService.retrieveTokenFromHeaderValue(null));
    }

    @Test
    void deveRetornarNuloComValorDoHeaderVazio() {
        assertNull(TokenService.retrieveTokenFromHeaderValue(""));
    }

    @Test
    void deveRetornarNuloComValorDoHeaderSemComecarComBearer() {
        assertNull(TokenService.retrieveTokenFromHeaderValue("tokenHeaderInvalido"));
    }

}
