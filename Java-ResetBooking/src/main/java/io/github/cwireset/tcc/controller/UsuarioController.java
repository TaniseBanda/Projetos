package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.service.CaminhoAvatar;
import io.github.cwireset.tcc.service.UsuarioService;
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
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario cadastrarUsuario(@RequestBody @Valid Usuario usuario) throws Exception {
        return usuarioService.cadastrarUsuario(usuario);
    }

    @GetMapping
    public Page<Usuario> listarUsuarios(@PageableDefault(sort = "nome", direction = Sort.Direction.ASC) @ApiIgnore Pageable pageable){
        return usuarioService.listarUsuarios(pageable);
    }

    @GetMapping("/{idUsuario}")
    public Usuario buscarUsuarioPorId(@PathVariable Long idUsuario) throws Exception {
        return usuarioService.buscarUsuarioPorId(idUsuario);
    }

    @GetMapping("/cpf/{cpf}")
    public Usuario buscarUsuarioPorCpf(@PathVariable String cpf) throws Exception {
        return usuarioService.buscarUsuarioPorCpf(cpf);
    }

    @PutMapping("/{id}")
    public Usuario alterarUsuario(@PathVariable Long id, @RequestBody @Valid Usuario usuario) throws Exception {
        return usuarioService.alterarUsuario(id, usuario);
    }
}
