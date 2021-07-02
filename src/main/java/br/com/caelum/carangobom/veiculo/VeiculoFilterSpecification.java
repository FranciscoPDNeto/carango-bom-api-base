package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.veiculo.dtos.VeiculoFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class VeiculoFilterSpecification implements Specification<Veiculo> {

    private final transient VeiculoFilterRequest veiculoFilter;
    private final transient List<Predicate> predicates;

    public VeiculoFilterSpecification(VeiculoFilterRequest veiculoFilter) {
        this.veiculoFilter = veiculoFilter;
        this.predicates = new ArrayList<>();
    }

    @Override
    public Predicate toPredicate(Root<Veiculo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        filterModelo(root, criteriaBuilder);
        filterMarca(root, criteriaBuilder);
        filterAno(root, criteriaBuilder);

        return criteriaBuilder.and(
            predicates.toArray(new Predicate[predicates.size()])
        );
    }

    private void filterAno(Root<Veiculo> root, CriteriaBuilder criteriaBuilder) {
        if(veiculoFilter.getAno() > 0) {
            predicates.add(
                criteriaBuilder.equal(
                    root.get("ano"), veiculoFilter.getAno()
                )
            );
        }
    }

    private void filterMarca(Root<Veiculo> root, CriteriaBuilder criteriaBuilder) {
        if(veiculoFilter.getMarcaId() > 0L) {
            predicates.add(
                criteriaBuilder.equal(
                    root.get("marca").get("id"), veiculoFilter.getMarcaId()
                )
            );
        }
    }

    private void filterModelo(Root<Veiculo> root, CriteriaBuilder criteriaBuilder) {
        if(!veiculoFilter.getModelo().isEmpty()) {
            predicates.add(
                criteriaBuilder.like(
                    root.get("modelo"), "%" + veiculoFilter.getModelo() + "%"
                )
            );
        }
    }
}
