package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.TipoAnuncio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CadastrarAnuncioRequest {

    @NotNull(message = "IdImovel é um campo obrigatório.")
    public Long idImovel;

    @NotNull (message = "IdAnunciante é um campo obrigatório.")
    public Long idAnunciante;

    @NotNull (message = "TipoAnuncio é um campo obrigatório.")
    public TipoAnuncio tipoAnuncio;

    @NotNull (message = "ValorDiaria é um campo obrigatório.")
    public BigDecimal valorDiaria;

    @NotNull(message = "FormasAceitas é um campo obrigatório.")
    public List<FormaPagamento> formasAceitas;

    @NotBlank (message = "Descricao é um campo obrigatório.")
    public String descricao;
}
