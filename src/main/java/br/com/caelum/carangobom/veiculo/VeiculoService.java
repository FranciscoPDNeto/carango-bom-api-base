package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.VeiculoNotFoundException;
import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoRequest;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VeiculoService {

    private VeiculoRepository veiculoRepository;

    public Veiculo getVeiculo(Long id) {
        Optional<Veiculo> optVeiculo = veiculoRepository.findById(id);
        return optVeiculo.orElseThrow(VeiculoNotFoundException::new);
    }

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

    public VeiculoResponse update(Long id, VeiculoRequest veiculoRequest) {
        var veiculo = getVeiculo(id);

        veiculo.setModelo(Objects.requireNonNullElse(veiculoRequest.getModelo(), veiculo.getModelo()));
        veiculo.setAno(Objects.requireNonNullElse(veiculoRequest.getAno(), veiculo.getAno()));
        veiculo.setValor(Objects.requireNonNullElse(veiculoRequest.getValor(), veiculo.getValor()));
        veiculo.setMarca(
            new Marca(
                Objects.requireNonNullElse(veiculoRequest.getMarcaId(), veiculo.getMarca().getId())
            )
        );

        var veiculoUpdated = veiculoRepository.save(veiculo);

        return VeiculoResponse.fromModel(veiculoUpdated);
    }

    public void delete(Long id) {
        var veiculo = getVeiculo(id);

        veiculoRepository.delete(veiculo);
    }
}
