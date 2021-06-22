package br.com.caelum.carangobom.validacao;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListaDeErrosOutputDto {
    private List<ErroDeParametroOutputDto> erros;
}
