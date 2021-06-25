package br.com.caelum.carangobom.exception;

public class UsuarioAlreadyRegisteredException extends RuntimeException {
    public UsuarioAlreadyRegisteredException() {
        super("Usuário já está cadastrado no sistema.");
    }
}
