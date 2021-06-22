package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("veiculos")
@Transactional
@AllArgsConstructor
public class VeiculoController {

    private VeiculoRepository veiculoRepository;

    @GetMapping
    public List<Veiculo> getAll() {
        return veiculoRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Validated @PathVariable Long id) {
        Optional<Veiculo> optVeiculo = veiculoRepository.findById(id);
        if (optVeiculo.isPresent()) {
            Veiculo veiculo = optVeiculo.get();
            veiculoRepository.delete(veiculo);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
