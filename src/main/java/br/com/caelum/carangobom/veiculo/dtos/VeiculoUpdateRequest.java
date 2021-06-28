package br.com.caelum.carangobom.veiculo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoUpdateRequest {

    private String modelo;

    private Integer ano;

    private Long valor;

    private Long marcaId;
}
