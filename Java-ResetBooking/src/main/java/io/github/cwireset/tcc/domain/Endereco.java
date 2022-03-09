package io.github.cwireset.tcc.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cep é um campo obrigatório.")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve ser informado no formato 99999-999.")
    private String cep;

    @NotBlank(message = "Logradouro é um campo obrigatório.")
    private String logradouro;

    @NotBlank (message = "Numero é um campo obrigatório.")
    private String numero;

    private String complemento;

    @NotBlank (message = "Bairro é um campo obrigatório.")
    private String bairro;

    @NotBlank (message = "Cidade é um campo obrigatório.")
    private String cidade;

    @NotBlank (message = "Estado é um campo obrigatório.")
    private String estado;

}
