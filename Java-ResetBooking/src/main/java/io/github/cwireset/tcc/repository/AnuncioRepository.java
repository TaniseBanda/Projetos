package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {

    boolean existsByImovelAndAtivo(Imovel imovel, Boolean ativo);

    Page<Anuncio> findByAtivo(Pageable pageable, Boolean ativo);

    Page<Anuncio> findByAnuncianteAndAtivo(Pageable pageable, Usuario anunciante, Boolean ativo);

    Anuncio findByIdEqualsAndAtivo(Long idAnuncio, Boolean ativo);

    boolean existsByIdAndAtivo(Long idAnuncio, Boolean ativo);
}
