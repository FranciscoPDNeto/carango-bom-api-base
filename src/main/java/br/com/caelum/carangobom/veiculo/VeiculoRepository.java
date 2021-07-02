package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.veiculo.dtos.VeiculoDashboard;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, JpaSpecificationExecutor<Veiculo> {

    @Query(
        "SELECT new br.com.caelum.carangobom.veiculo.dtos.VeiculoDashboard (COUNT(v.marca), SUM(v.valor), m.nome) " +
        "FROM Veiculo v INNER JOIN Marca m ON v.marca.id=m.id group by v.marca.id"
    )
    List<VeiculoDashboard> groupVeiculosByMarca();

    List<Veiculo> findAll(Specification<Veiculo> specification);
}
