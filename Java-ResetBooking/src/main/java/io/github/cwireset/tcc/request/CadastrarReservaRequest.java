package io.github.cwireset.tcc.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.cwireset.tcc.domain.Periodo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class CadastrarReservaRequest {

    @NotNull(message = "IdSolicitante é um campo obrigatório.")
    public Long idSolicitante;

    @NotNull(message = "IdAnuncio é um campo obrigatório.")
    public Long idAnuncio;

    @NotNull(message = "Periodo é um campo obrigatório.")
    public Periodo periodo;

    @NotNull(message = "QuantidadePessoas é um campo obrigatório.")
    public Integer quantidadePessoas;
}
