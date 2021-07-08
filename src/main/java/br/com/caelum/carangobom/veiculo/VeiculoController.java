package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.VeiculoNotFoundException;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoDashboard;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoFilterRequest;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoRequest;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoResponse;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoUpdateRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("veiculos")
@Transactional
@AllArgsConstructor
public class VeiculoController {

    private VeiculoService veiculoService;

    @ApiImplicitParams({
        @ApiImplicitParam(name = "modelo", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "ano", dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "marcaId", dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "valor>", dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "valor<", dataType = "long", paramType = "query")
    })
    @GetMapping
    public ResponseEntity<List<VeiculoResponse>> getAll(
        @RequestParam @ApiIgnore Map<String, String> params
    ) {
        var veiculoFilter = new VeiculoFilterRequest(
            params.getOrDefault("modelo", ""),
            Integer.valueOf(params.getOrDefault("ano", "0")),
            Long.valueOf(params.getOrDefault("marcaId", "0")),
            Long.valueOf(params.getOrDefault("valor>", "0")),
            Long.valueOf(params.getOrDefault("valor<", String.valueOf(Long.MAX_VALUE)))
        );
        return ResponseEntity.ok(veiculoService.findAll(veiculoFilter));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<VeiculoDashboard>> dashboard() {
        return ResponseEntity.ok(veiculoService.dashboard());
    }

    @PostMapping
    public ResponseEntity<VeiculoResponse> save(
        @RequestBody @Valid VeiculoRequest veiculoRequest,
        UriComponentsBuilder uriBuilder
    ) {
       var veiculoResponse = veiculoService.save(veiculoRequest);

       var uri = uriBuilder
           .path("veiculos/{id}")
           .buildAndExpand(veiculoResponse.getId())
           .toUri();

       return ResponseEntity.created(uri).body(veiculoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> update(
        @Validated @PathVariable Long id,
        @RequestBody @Valid VeiculoUpdateRequest veiculoRequest
    ) {
        try {
            var veiculoResponse = veiculoService.update(id, veiculoRequest);
            return ResponseEntity.ok(veiculoResponse);
        } catch (VeiculoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
