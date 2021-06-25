package br.com.caelum.carangobom.marca.dtos;

import br.com.caelum.carangobom.marca.Marca;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MarcaRequest {
    @NotBlank
    @Size(min = 2, message = "Deve ter {min} ou mais caracteres.")
    private String nome;

    public Marca toModel() {
        return new Marca(nome);
    }
}
