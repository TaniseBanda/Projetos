package io.github.cwireset.tcc.response;

import io.github.cwireset.tcc.domain.Pagamento;
import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InformacaoReservaResponse {

    public Long idReserva;

    public DadosSolicitanteResponse solicitante;

    public Integer quantidadePessoas;

    public DadosAnuncioResponse anuncio;

    public Periodo periodo;

    public Pagamento pagamento;

}
