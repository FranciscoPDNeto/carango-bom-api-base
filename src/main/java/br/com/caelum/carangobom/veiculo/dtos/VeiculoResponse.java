package br.com.caelum.carangobom.veiculo.dtos;

import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import br.com.caelum.carangobom.veiculo.Veiculo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VeiculoResponse {

    private Long id;
    private String modelo;
    private Integer ano;
    private Long valor;
    private MarcaResponse marca;

    public VeiculoResponse(Long id) {
        this.id = id;
    }

    public static VeiculoResponse fromModel(Veiculo veiculo) {
        return new VeiculoResponse(
            veiculo.getId(),
            veiculo.getModelo(),
            veiculo.getAno(),
            veiculo.getValor(),
            MarcaResponse.fromModel(veiculo.getMarca())
        );
    }
}
