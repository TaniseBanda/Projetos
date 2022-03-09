package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImovelRepository extends JpaRepository<Imovel, Long> {

    Page<Imovel> findByAtivo(Pageable pageable, Boolean ativo);

    Page<Imovel> findByProprietarioEqualsAndAtivo(Pageable pageable, Usuario usuario, Boolean ativo);

    Imovel findByIdEqualsAndAtivo(Long idImovel, Boolean ativo);

    boolean existsByIdAndAtivo(Long idImovel, Boolean ativo);

}
