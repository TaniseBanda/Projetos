package io.github.cwireset.tcc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoAnuncio tipoAnuncio;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_imovel")
    private Imovel imovel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_anunciante")
    private Usuario anunciante;

    private BigDecimal valorDiaria;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<FormaPagamento> formasAceitas;

    private String descricao;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean ativo;

}
