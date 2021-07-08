package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.controller.BaseCrudController;
import br.com.caelum.carangobom.marca.dtos.MarcaRequest;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import br.com.caelum.carangobom.service.BaseCrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/marcas")
public class MarcaController extends BaseCrudController<MarcaResponse, MarcaRequest> {

    private MarcaService marcaService;

    @Override
    @Cacheable(value = "brandList")
    public ResponseEntity<List<MarcaResponse>> getAll() {
        return super.getAll();
    }

    @Override
    @Cacheable(value = "brand")
    public ResponseEntity<MarcaResponse> getById(@Validated @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @CacheEvict(value = {"brand", "brandList"}, allEntries = true)
    public ResponseEntity<MarcaResponse> save(@Valid @RequestBody MarcaRequest marcaRequest, UriComponentsBuilder uriBuilder) {
        return super.save(marcaRequest, uriBuilder);
    }

    @Override
    @CacheEvict(value = {"brand", "brandList"}, allEntries = true)
    public ResponseEntity<MarcaResponse> update(@Validated @PathVariable Long id, @Valid @RequestBody MarcaRequest marcaRequest) {
        return super.update(id, marcaRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Validated @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    public BaseCrudService<MarcaResponse, MarcaRequest> getService() {
        return marcaService;
    }

    @Override
    public URI getPathFromResponse(MarcaResponse response, UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.path("/marcas/{id}").buildAndExpand(response.getId()).toUri();
    }
}