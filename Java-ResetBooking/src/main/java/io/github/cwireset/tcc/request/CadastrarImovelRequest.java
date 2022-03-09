package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CadastrarImovelRequest {

    @NotBlank(message = "Identificacao é um campo obrigatório.")
    private String identificacao;

    @NotNull(message = "TipoImovel é um campo obrigatório.")
    private TipoImovel tipoImovel;

    @NotNull (message = "Endereco é um campo obrigatório.")
    private Endereco endereco;

    @NotNull(message = "IdProprietario é um campo obrigatório.")
    private Long idProprietario;

    private List<CaracteristicaImovel> caracteristicas;


}
