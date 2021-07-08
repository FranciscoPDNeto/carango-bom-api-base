package br.com.caelum.carangobom.basecrud;

import java.util.List;

public interface BaseCrudService<T1, T2> {
    List<T1> findAll();

    T1 findById(Long id);

    T1 save(T2 request);

    void delete(Long id);

    T1 update(Long id, T2 request);
}
