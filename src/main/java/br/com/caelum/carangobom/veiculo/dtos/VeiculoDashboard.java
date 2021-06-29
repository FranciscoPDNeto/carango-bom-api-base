package br.com.caelum.carangobom.veiculo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VeiculoDashboard {

    private Long numVeiculos;
    private Long somaValor;
    private String marca;
}
