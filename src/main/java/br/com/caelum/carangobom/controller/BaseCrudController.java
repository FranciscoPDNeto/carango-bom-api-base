package br.com.caelum.carangobom.controller;

import br.com.caelum.carangobom.service.BaseCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Transactional
public abstract class BaseCrudController<T1, T2> {

    @GetMapping
    public ResponseEntity<List<T1>> getAll() {
        return ResponseEntity.ok(getService().findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<T1> getById(@Validated @PathVariable Long id) {
        try {
            T1 response = getService().findById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<T1> save(@Valid @RequestBody T2 request, UriComponentsBuilder uriComponentsBuilder) {
        try {
            var response = getService().save(request);
            var uri = getPathFromResponse(response, uriComponentsBuilder);

            return ResponseEntity.created(uri).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Validated @PathVariable Long id) {
        try {
            getService().delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<T1> update(@Validated @PathVariable Long id, @RequestBody @Valid T2 request) {
        try {
            var response = getService().update(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



    public abstract BaseCrudService<T1, T2> getService();

    public abstract URI getPathFromResponse(T1 response, UriComponentsBuilder uriComponentsBuilder);
}
