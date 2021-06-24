package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.VeiculoNotFoundException;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoRequest;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VeiculoService {

    private VeiculoRepository veiculoRepository;

    public List<VeiculoResponse> findAll() {
        return veiculoRepository.findAll()
            .stream()
            .map(VeiculoResponse::fromModel)
            .collect(Collectors.toList());
    }

    public VeiculoResponse save(VeiculoRequest veiculoRequest) {
        var veiculo = veiculoRequest.toModel();

        return VeiculoResponse.fromModel(veiculoRepository.save(veiculo));
    }

    public void delete(Long id) {
        Optional<Veiculo> optVeiculo = veiculoRepository.findById(id);
        var veiculo = optVeiculo.orElseThrow(VeiculoNotFoundException::new);

        veiculoRepository.delete(veiculo);
    }
}
