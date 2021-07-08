package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.MarcaNotFoundException;
import br.com.caelum.carangobom.marca.dtos.MarcaRequest;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/marcas")
@Transactional
@AllArgsConstructor
public class MarcaController {

    private MarcaService marcaService;

    @GetMapping
    @Cacheable(value = "brandList")
    public ResponseEntity<List<MarcaResponse>> getAllByNameOrder() {
        return ResponseEntity.ok(marcaService.findAllByNameOrder());
    }

    @GetMapping("/{id}")
    @Cacheable(value = "brand")
    public ResponseEntity<MarcaResponse> getById(@Validated @PathVariable Long id) {
        try {
            var marcaResponse = marcaService.findById(id);
            return ResponseEntity.ok(marcaResponse);
        } catch (MarcaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @CacheEvict(value = {"brand", "brandList"}, allEntries = true)
    public ResponseEntity<MarcaResponse> save(@Valid @RequestBody MarcaRequest marcaRequest, UriComponentsBuilder uriBuilder) {
        var marcaResponse = marcaService.save(marcaRequest);
        var uri = uriBuilder.path("/marcas/{id}").buildAndExpand(marcaResponse.getId()).toUri();

        return ResponseEntity.created(uri).body(marcaResponse);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = {"brand", "brandList"}, allEntries = true)
    public ResponseEntity<MarcaResponse> update(@Validated @PathVariable Long id, @Valid @RequestBody MarcaRequest marcaRequest) {

        try {
            var marcaResponse = marcaService.update(id, marcaRequest);
            return ResponseEntity.ok(marcaResponse);
        } catch (MarcaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = {"brand", "brandList"}, allEntries = true)
    public ResponseEntity<Void> delete(@Validated @PathVariable Long id) {
        try {
            marcaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (MarcaNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}