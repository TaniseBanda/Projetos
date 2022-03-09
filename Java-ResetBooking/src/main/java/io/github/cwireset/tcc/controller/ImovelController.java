package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import io.github.cwireset.tcc.service.ImovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Imovel CadastrarImovel(@RequestBody @Valid CadastrarImovelRequest cadastrarImovelRequest) throws Exception {
        return imovelService.cadastrarImovel(cadastrarImovelRequest);
    }

    @GetMapping
    public Page<Imovel> ListarImoveis(@PageableDefault @ApiIgnore Pageable pageable) {
        return imovelService.listarImovel(pageable);
    }

    @GetMapping("/proprietarios/{idProprietario}")
    public Page<Imovel> listarImoveisPorProprietario(@PageableDefault @ApiIgnore Pageable pageable, @PathVariable Long idProprietario) throws Exception {
        return imovelService.listarImoveisPorProprietario(pageable, idProprietario);
    }

    @GetMapping ("/{idImovel}")
    public Imovel buscarImovelPorId(@PathVariable Long idImovel) throws Exception {
        return imovelService.buscarImovelPorId(idImovel);
    }

    @DeleteMapping("/{idImovel}")
    public void excluirImovel(@PathVariable Long idImovel) throws Exception {
        imovelService.excluirImovel(idImovel);
    }

}
