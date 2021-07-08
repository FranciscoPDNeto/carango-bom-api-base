package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.basecrud.BaseCrudController;
import br.com.caelum.carangobom.marca.dtos.MarcaRequest;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import lombok.AllArgsConstructor;
import br.com.caelum.carangobom.basecrud.BaseCrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/marcas")
public class MarcaController extends BaseCrudController<MarcaResponse, MarcaRequest> {

    private MarcaService marcaService;

    @Override
    protected boolean useCache() {
        return true;
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