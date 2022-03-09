package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.service.AnuncioService;
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
@RequestMapping("/anuncios")

public class AnuncioController {

    @Autowired
    private AnuncioService anuncioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
        public Anuncio AnunciarImovel(@RequestBody @Valid CadastrarAnuncioRequest cadastrarAnuncioRequest) throws Exception {
        return anuncioService.anunciarImovel(cadastrarAnuncioRequest);
    }

    @GetMapping
    public Page<Anuncio> ListarAnuncios(@PageableDefault(sort = "valorDiaria", direction = Sort.Direction.ASC) @ApiIgnore Pageable pageable) {
        return anuncioService.listarAnuncios(pageable);
    }

    @GetMapping("/anunciantes/{idAnunciante}")
    public Page<Anuncio> listarAnunciosPorAnunciante(@PageableDefault(sort = "valorDiaria", direction = Sort.Direction.ASC) @ApiIgnore Pageable pageable, @PathVariable @Valid Long idAnunciante) throws Exception {
        return anuncioService.listarAnunciosPorAnunciante(pageable, idAnunciante);
    }

    @DeleteMapping("/{idAnuncio}")
    public void excluirAnuncio(@PathVariable @Valid Long idAnuncio) throws Exception {
        anuncioService.excluirAnuncio(idAnuncio);
    }

}
