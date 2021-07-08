package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.MarcaNotFoundException;
import br.com.caelum.carangobom.marca.dtos.MarcaRequest;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import br.com.caelum.carangobom.basecrud.BaseCrudService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MarcaService implements BaseCrudService<MarcaResponse, MarcaRequest> {

    private MarcaRepository marcaRepository;

    public List<MarcaResponse> findAllByNameOrder() {
        return marcaRepository.findAllByOrderByNome()
            .stream()
            .map(MarcaResponse::fromModel)
            .collect(Collectors.toList());
    }

    @Override
    public List findAll() {
        return findAllByNameOrder();
    }

    public MarcaResponse findById(Long id) {
        Optional<Marca> marcaOptional = marcaRepository.findById(id);
        var marca = marcaOptional.orElseThrow(MarcaNotFoundException::new);

        return MarcaResponse.fromModel(marca);
    }

    @Override
    public MarcaResponse save(MarcaRequest marcaRequest) {
        return MarcaResponse.fromModel(marcaRepository.save(marcaRequest.toModel()));
    }

    @Override
    public MarcaResponse update(Long id, MarcaRequest marcaRequest) {
        var marcaOptional = marcaRepository.findById(id);
        var marca = marcaOptional.orElseThrow(MarcaNotFoundException::new);
        marca.setNome(marcaRequest.getNome());

        return MarcaResponse.fromModel(marca);
    }

    public void delete(Long id) {
        var marcaOptional = marcaRepository.findById(id);
        var marca = marcaOptional.orElseThrow(MarcaNotFoundException::new);

        marcaRepository.delete(marca);
    }
}
