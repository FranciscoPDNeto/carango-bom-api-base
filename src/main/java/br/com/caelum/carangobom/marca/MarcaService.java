package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.MarcaNotFoundException;
import br.com.caelum.carangobom.marca.dtos.MarcaRequest;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    public List<MarcaResponse> findAllByNameOrder() {
        return marcaRepository.findAllByOrderByNome()
            .stream()
            .map(MarcaResponse::fromModel)
            .collect(Collectors.toList());
    }

    public MarcaResponse findById(Long id) {
        Optional<Marca> marca = marcaRepository.findById(id);
        if (marca.isPresent())
            return MarcaResponse.fromModel(marca.get());

        return null;
    }

    public MarcaResponse save(MarcaRequest marcaRequest) {
        return MarcaResponse.fromModel(marcaRepository.save(marcaRequest.toModel()));
    }

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
