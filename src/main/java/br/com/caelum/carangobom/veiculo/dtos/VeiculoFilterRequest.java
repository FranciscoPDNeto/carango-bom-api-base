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
    private Long valorMaior = 0L;
    private Long valorMenor = Long.MAX_VALUE;
}
