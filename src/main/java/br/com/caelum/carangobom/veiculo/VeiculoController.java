package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.VeiculoNotFoundException;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("veiculos")
@Transactional
@AllArgsConstructor
public class VeiculoController {

    private VeiculoService veiculoService;

    @GetMapping
    public List<VeiculoResponse> getAll() {
        return veiculoService.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Validated @PathVariable Long id) {
        try {
            veiculoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (VeiculoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
