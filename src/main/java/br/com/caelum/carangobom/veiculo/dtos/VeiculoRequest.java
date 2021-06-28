package br.com.caelum.carangobom.veiculo.dtos;

import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.veiculo.Veiculo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoRequest {

    @NotBlank
    private String modelo;

    @NonNull
    @Positive
    private Integer ano;

    @NonNull
    @Positive
    private Long valor;

    @NonNull
    private Long marcaId;

     public Veiculo toModel() {
        var marca = new Marca(this.getMarcaId());

        return new Veiculo(this.getModelo(), this.getAno(), this.getValor(), marca);
    }
}
