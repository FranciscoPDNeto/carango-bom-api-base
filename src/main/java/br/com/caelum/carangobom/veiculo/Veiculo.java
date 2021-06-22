package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

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

    @NonNull
    @Positive
    private Integer ano;

    @NonNull
    @Positive
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "MARCA_ID")
    private Marca marca;

    public Veiculo(Long id) {
        this.id = id;
    }

    public Veiculo(String modelo, Integer ano, BigDecimal valor, Marca marca) {
        this.modelo = modelo;
        this.ano = ano;
        this.valor = valor;
        this.marca = marca;
    }


}
