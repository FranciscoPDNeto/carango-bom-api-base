package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String modelo;

    @NotNull
    @Positive
    private Integer ano;

    @NotNull
    @Positive
    private Long valor;

    @ManyToOne
    @JoinColumn(name = "MARCA_ID")
    private Marca marca;

    public Veiculo(Long id) {
        this.id = id;
    }

    public Veiculo(String modelo, Integer ano, Long valor, Marca marca) {
        this.modelo = modelo;
        this.ano = ano;
        this.valor = valor;
        this.marca = marca;
    }
}
