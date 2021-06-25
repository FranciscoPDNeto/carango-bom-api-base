package br.com.caelum.carangobom.marca;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
public class Marca {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, message = "Deve ter {min} ou mais caracteres.")
    private String nome;

    public Marca(String nome) {
        this(null, nome);
    }

    public Marca(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Marca(Long id) {
        this.id = id;
    }
}
