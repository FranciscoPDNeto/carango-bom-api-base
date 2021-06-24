package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.VeiculoNotFoundException;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoRequest;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("veiculos")
@Transactional
@AllArgsConstructor
public class VeiculoController {

    private VeiculoService veiculoService;

    @GetMapping
    public ResponseEntity<List<VeiculoResponse>> getAll() {
        return ResponseEntity.ok(veiculoService.findAll());
    }

    @PostMapping
    public ResponseEntity<VeiculoResponse> save(
        @RequestBody @Valid VeiculoRequest veiculoRequest,
        UriComponentsBuilder uriBuilder
    ) {
       VeiculoResponse veiculoResponse = veiculoService.save(veiculoRequest);

        URI uri = uriBuilder
            .path("veiculos/{id}")
            .buildAndExpand(veiculoResponse.getId())
            .toUri();

       return ResponseEntity.created(uri).body(veiculoResponse);
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
