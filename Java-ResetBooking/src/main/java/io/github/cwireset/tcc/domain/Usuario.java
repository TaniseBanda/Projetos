package io.github.cwireset.tcc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "Nome é um campo obrigatório.")
    private String nome;

    @NotBlank (message = "E-mail é um campo obrigatório.")
    private String email;

    @NotBlank (message = "Senha é um campo obrigatório.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;


    @NotBlank (message = "Cpf é um campo obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ser informado no formato 99999999999.")
    private String cpf;

    @NotNull (message = "Data de nascimento é um campo obrigatório.")
    private LocalDate dataNascimento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    @Valid
    private Endereco endereco;

    private String avatar;

}
