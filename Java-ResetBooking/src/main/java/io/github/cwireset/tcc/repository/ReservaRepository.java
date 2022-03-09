package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAll();

    Reserva findByIdEquals(Long idReserva);

    Page<Reserva> findBySolicitante_Id(Pageable pageable, Long idSolicitante);

    Page<Reserva> findBySolicitante_IdAndPeriodo_DataHoraInicialBetweenAndPeriodo_DataHoraFinalBetween(Pageable pageable,Long idSolicitante, LocalDateTime dataHoraInicial, LocalDateTime dataHoraFinal, LocalDateTime dataHoraInicial1, LocalDateTime dataHoraFinal1);

    boolean existsByPeriodo_DataHoraInicialLessThanAndPeriodo_DataHoraFinalGreaterThanAndAnuncio_IdEqualsAndPagamento_StatusIn(LocalDateTime dataHoraFinal, LocalDateTime dataHoraInicial, Long idAnuncio, List<StatusPagamento> status);

    Page<Reserva> findByAnuncio_Anunciante_Id(Pageable pageable,Long idAnunciante);

}



