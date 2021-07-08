package br.com.caelum.carangobom.veiculo.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoFilterRequest {

    private String modelo = "";
    private Integer ano = 0;
    private Long marcaId = 0L;
    private Long valorMaiorIgual = 0L;
    private Long valorMenorIgual = Long.MAX_VALUE;
}
