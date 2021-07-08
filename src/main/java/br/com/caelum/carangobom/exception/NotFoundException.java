package br.com.caelum.carangobom.exception;

public class NotFoundException extends RuntimeException {
    NotFoundException(String elementNameNotFound) {
        super(elementNameNotFound + " não encontrado.");
    }
}
