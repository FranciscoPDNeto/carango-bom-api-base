package br.com.caelum.carangobom.exception;

public class MarcaNotFoundException extends RuntimeException {
    public MarcaNotFoundException() {
        super("Marca não encontrado.");
    }
}
