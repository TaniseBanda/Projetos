package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.exception.ExceptionStatus400;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import io.github.cwireset.tcc.response.DadosAnuncioResponse;
import io.github.cwireset.tcc.response.DadosSolicitanteResponse;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import io.github.cwireset.tcc.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InformacaoReservaResponse RealizarReserva (@RequestBody @Valid CadastrarReservaRequest cadastrarReservaRequest) throws Exception {

        Reserva reserva = reservaService.realizarReserva(cadastrarReservaRequest);

        DadosSolicitanteResponse responseDadosSolicitante = new DadosSolicitanteResponse(reserva.getSolicitante().getId(), reserva.getSolicitante().getNome());
        DadosAnuncioResponse responseDadosAnuncio = new DadosAnuncioResponse(reserva.getAnuncio().getId(),reserva.getAnuncio().getImovel(), reserva.getAnuncio().getAnunciante(), reserva.getAnuncio().getFormasAceitas(), reserva.getAnuncio().getDescricao());

        InformacaoReservaResponse responseRealizarReserva = new InformacaoReservaResponse();
        responseRealizarReserva.setIdReserva(reserva.getId());
        responseRealizarReserva.setSolicitante(responseDadosSolicitante);
        responseRealizarReserva.setQuantidadePessoas(reserva.getQuantidadePessoas());
        responseRealizarReserva.setAnuncio(responseDadosAnuncio);
        responseRealizarReserva.setPeriodo(reserva.getPeriodo());
        responseRealizarReserva.setPagamento(reserva.getPagamento());

        return responseRealizarReserva;
    }


    @GetMapping("/{idReserva}")
    public Reserva buscarReservaPorId(@PathVariable @Valid Long idReserva) throws Exception {
        return reservaService.buscarReservaPorId(idReserva);
    }

    @GetMapping("/solicitantes/{idSolicitante}")
    public Page<Reserva> ListarReservaPorSolicitante(@PageableDefault (sort = "periodo.dataHoraFinal", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @PathVariable Long idSolicitante, String dataHoraInicial, String dataHoraFinal) {


        return reservaService.listarReservaPorSolicitante(pageable, idSolicitante, dataHoraInicial, dataHoraFinal);
    }

    @GetMapping("/anuncios/anunciantes/{idAnunciante}")
    public Page<Reserva> ListarReservaPorAnunciante(@PageableDefault(sort = "periodo.dataHoraFinal", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @PathVariable Long idAnunciante) throws Exception {
        return reservaService.listarReservaPorAnunciante(pageable, idAnunciante);
    }

    @PutMapping("/{idReserva}/pagamentos")
    public void PagarReserva(@PathVariable Long idReserva, @RequestBody FormaPagamento formaPagamento) throws Exception {
        reservaService.pagarReserva(idReserva, formaPagamento);
    }

    @PutMapping("/{idReserva}/pagamentos/cancelar")
    public void CancelarReserva(@PathVariable Long idReserva) throws Exception {
        reservaService.cancelarReserva(idReserva);
    }

    @PutMapping("/{idReserva}/pagamentos/estornar")
    public void EstornarReserva(@PathVariable Long idReserva) throws Exception {
        reservaService.estornarReserva(idReserva);
    }

}
