package br.com.caelum.carangobom.validacao;

import lombok.Data;

@Data
public class ErroDeParametroOutputDto {
    private final String parametro;
    private final String mensagem;
}
