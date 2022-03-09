package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.ExceptionStatus400;
import io.github.cwireset.tcc.exception.ExceptionStatus404;
import io.github.cwireset.tcc.repository.ReservaRepository;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReservaService {
    
    private ReservaRepository reservaRepository;
    private AnuncioService anuncioService;
    private UsuarioService usuarioService;



    @Autowired
    public ReservaService(ReservaRepository reservaRepository, AnuncioService anuncioService, UsuarioService usuarioService) {
        this.reservaRepository = reservaRepository;
        this.anuncioService = anuncioService;
        this.usuarioService = usuarioService;
    }


    public Reserva realizarReserva(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {

        // anuncio - buscar no banco e exceção anuncio não encontrado
        Anuncio anuncio = anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());

        // solicitante - buscar no banco e exceção usuario não encontrado
        Usuario solicitante = usuarioService.buscarUsuarioPorId(cadastrarReservaRequest.getIdSolicitante());


        Pagamento pagamento = new Pagamento();

        Reserva reserva = new Reserva();
        reserva.setSolicitante(solicitante);
        reserva.setAnuncio(anuncio);
        reserva.setPeriodo(cadastrarReservaRequest.getPeriodo());
        reserva.setQuantidadePessoas(cadastrarReservaRequest.getQuantidadePessoas());
        reserva.setDataHoraReserva(LocalDateTime.now());
        reserva.setPagamento(pagamento);
        reserva.getPagamento().setStatus(StatusPagamento.PENDENTE);

        //Validar e sobrescrever horario inicio da reserva
        LocalDateTime dataHoraInicio = cadastrarReservaRequest.getPeriodo().getDataHoraInicial();
        LocalDate dataInicio = dataHoraInicio.toLocalDate();
        LocalTime horaInicio = dataHoraInicio.toLocalTime();
        if (horaInicio != LocalTime.of(14,00,00)){
            cadastrarReservaRequest.getPeriodo().setDataHoraInicial(LocalDateTime.of(dataInicio, LocalTime.of(14, 00, 00)));
        }

        //Validar e sobrescrever horario final da reserva
        LocalDateTime dataHoraFim = cadastrarReservaRequest.getPeriodo().getDataHoraFinal();
        LocalDate dataFim = dataHoraFim.toLocalDate();
        LocalTime horaFim = dataHoraFim.toLocalTime();
        if (horaFim != LocalTime.of(12,00,00)){
            cadastrarReservaRequest.getPeriodo().setDataHoraFinal(LocalDateTime.of(dataFim, LocalTime.of(12, 00, 00)));
        }

        //Calcular valor para pagamento
        long qtdDiarias = dataInicio.until(dataFim, ChronoUnit.DAYS);
        BigDecimal vlrDiaria = anuncio.getValorDiaria();
        BigDecimal vlrTotal = vlrDiaria.multiply(new BigDecimal(qtdDiarias));
        pagamento.setValorTotal(vlrTotal);

        // exceção dataInicio > dataFim
        if (dataFim.isBefore(dataInicio)) {
            throw new ExceptionStatus400("Período inválido! A data final da reserva precisa ser maior do que a data inicial.");
        }

        // exceção reserva < 1 dia
        if(dataFim.isEqual(dataInicio)) {
            throw new ExceptionStatus400("Período inválido! O número mínimo de diárias precisa ser maior ou igual à 1.");
        }

        // exceção solicitante == anunciante
        if(solicitante.getId().equals(anuncio.getAnunciante().getId())){
            throw new ExceptionStatus400("O solicitante de uma reserva não pode ser o próprio anunciante.");
        }

        // exceção imovel com reserva ativa mesmo período
        List<StatusPagamento> status = new ArrayList<>();
        status.add(StatusPagamento.PENDENTE);
        status.add(StatusPagamento.PAGO);

        if (( reservaRepository.existsByPeriodo_DataHoraInicialLessThanAndPeriodo_DataHoraFinalGreaterThanAndAnuncio_IdEqualsAndPagamento_StatusIn (cadastrarReservaRequest.getPeriodo().getDataHoraFinal(),cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getIdAnuncio(), status)
                )) {
            throw new ExceptionStatus400("Este anuncio já esta reservado para o período informado.");
        }

        // exceção Hotel - mínimo 2 pessoas
        Integer minPessoas = 2;
        Integer qtdPessoas = cadastrarReservaRequest.getQuantidadePessoas();
        if (anuncio.getImovel().getTipoImovel().equals(TipoImovel.HOTEL)){
            if (qtdPessoas < minPessoas){
                throw new ExceptionStatus400(String.format("Não é possivel realizar uma reserva com menos de %d pessoas para imóveis do tipo Hotel", minPessoas));
            }
        }

        // exceção Pousada - mínimo 5 diárias
        Integer minDiarias = 5;
        if (anuncio.getImovel().getTipoImovel().equals(TipoImovel.POUSADA)){
            if (qtdDiarias < minDiarias ){
                throw new ExceptionStatus400(String.format("Não é possivel realizar uma reserva com menos de %d diárias para imóveis do tipo Pousada", minDiarias));
            }
        }

        return reservaRepository.save(reserva);
    }

    public Page<Reserva> listarReservaPorSolicitante(Pageable pageable,Long idSolicitante, String dataHoraInicial, String dataHoraFinal) {

        if(dataHoraInicial == null || dataHoraFinal == null) {
            return reservaRepository.findBySolicitante_Id(pageable, idSolicitante);
        }
            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime inicio = LocalDateTime.parse(dataHoraInicial, formatoData);
            LocalDateTime fim = LocalDateTime.parse(dataHoraFinal, formatoData);
            return reservaRepository.findBySolicitante_IdAndPeriodo_DataHoraInicialBetweenAndPeriodo_DataHoraFinalBetween(pageable,idSolicitante, inicio, fim, inicio, fim);
    }

    public Page<Reserva> listarReservaPorAnunciante(Pageable pageable,Long idAnunciante) throws Exception {

        Usuario anunciante = usuarioService.buscarUsuarioPorId(idAnunciante);
        return reservaRepository.findByAnuncio_Anunciante_Id(pageable, idAnunciante);

    }


    public Reserva buscarReservaPorId(Long idReserva) throws Exception{
        if(!reservaRepository.existsById(idReserva)){
            throw new ExceptionStatus404(String.format("Nenhum(a) Reserva com Id com o valor '%d' foi encontrado.", idReserva));
        }
                return reservaRepository.findByIdEquals(idReserva);
    }


    public void pagarReserva(Long idReserva, FormaPagamento formaPagamento) throws Exception {
        Reserva reservaBd = buscarReservaPorId(idReserva);

        if (!reservaBd.getAnuncio().getFormasAceitas().contains(formaPagamento)){
            throw new ExceptionStatus400(String.format("O anúncio não aceita %s como forma de pagamento. As formas aceitas são %s.", formaPagamento, reservaBd.getAnuncio().getFormasAceitas().toString().replace("[","").replace("]","")));
        }
        if (!reservaBd.getPagamento().getStatus().equals(StatusPagamento.PENDENTE)){
            throw new ExceptionStatus400("Não é possível realizar o pagamento para esta reserva, pois ela não está no status PENDENTE.");
        }
        reservaBd.getPagamento().setStatus(StatusPagamento.PAGO);
        reservaBd.getPagamento().setFormaEscolhida(formaPagamento);
        reservaRepository.save(reservaBd);
    }

    public void cancelarReserva(Long idReserva) throws Exception {
        Reserva reservaBd = buscarReservaPorId(idReserva);
        if (!reservaBd.getPagamento().getStatus().equals(StatusPagamento.PENDENTE)){
            throw new ExceptionStatus400("Não é possível realizar o cancelamento para esta reserva, pois ela não está no status PENDENTE.");
        }
        reservaBd.getPagamento().setStatus(StatusPagamento.CANCELADO);
        reservaRepository.save(reservaBd);
    }

    public void estornarReserva(Long idReserva) throws Exception {
        Reserva reservaBd = buscarReservaPorId(idReserva);
        if (!reservaBd.getPagamento().getStatus().equals(StatusPagamento.PAGO)){
            throw new ExceptionStatus400("Não é possível realizar o estorno para esta reserva, pois ela não está no status PAGO.");
        }
        reservaBd.getPagamento().setStatus(StatusPagamento.ESTORNADO);
        reservaBd.getPagamento().setFormaEscolhida(null);
        reservaRepository.save(reservaBd);
    }

}
