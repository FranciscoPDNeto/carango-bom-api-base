package br.com.caelum.carangobom.marca.dtos;

import br.com.caelum.carangobom.marca.Marca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarcaRequest {
    @NotBlank
    @Size(min = 2, message = "Deve ter {min} ou mais caracteres.")
    private String nome;

    public Marca toModel() {
        return new Marca(nome);
    }
}
