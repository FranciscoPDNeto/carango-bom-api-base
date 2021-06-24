package br.com.caelum.carangobom.exception;

public class VeiculoNotFoundException extends RuntimeException {
    public VeiculoNotFoundException() {
        super("Veículo não encontrado.");
    }
}
