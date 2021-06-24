package br.com.caelum.carangobom.marca.dtos;

import br.com.caelum.carangobom.marca.Marca;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MarcaResponse {

    private Long id;
    private String nome;

    public static MarcaResponse fromModel(Marca marca) {
        return new MarcaResponse(marca.getId(), marca.getNome());
    }
}
