package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.ExceptionStatus400;
import io.github.cwireset.tcc.exception.ExceptionStatus404;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ImovelService {


    private ImovelRepository imovelRepository;
    private UsuarioService usuarioService;
    private ExisteAnuncioService existeAnuncioService;

    @Autowired
    public ImovelService(ImovelRepository imovelRepository, UsuarioService usuarioService, ExisteAnuncioService existeAnuncioService) {
        this.imovelRepository = imovelRepository;
        this.usuarioService = usuarioService;
        this.existeAnuncioService = existeAnuncioService;
    }


    public Imovel cadastrarImovel(CadastrarImovelRequest cadastrarImovelRequest) throws Exception{
        Usuario usuario = usuarioService.buscarUsuarioPorId(cadastrarImovelRequest.getIdProprietario());
        Imovel imovel = new Imovel();
        imovel.setTipoImovel(cadastrarImovelRequest.getTipoImovel());
        imovel.setCaracteristicas(cadastrarImovelRequest.getCaracteristicas());
        imovel.setEndereco(cadastrarImovelRequest.getEndereco());
        imovel.setProprietario(usuario);
        imovel.setCaracteristicas(cadastrarImovelRequest.getCaracteristicas());
        imovel.setIdentificacao(cadastrarImovelRequest.getIdentificacao());
        imovel.setAtivo(true);
        return imovelRepository.save(imovel);
    }

    public Page<Imovel> listarImovel(Pageable pageable) {
        return imovelRepository.findByAtivo(pageable,true);
    }

    public Page<Imovel> listarImoveisPorProprietario(Pageable pageable, Long idProprietario) throws Exception {
        Usuario usuario = usuarioService.buscarUsuarioPorId(idProprietario);

        return imovelRepository.findByProprietarioEqualsAndAtivo(pageable, usuario, true);
    }

    public Imovel buscarImovelPorId(Long idImovel) throws Exception {
        if(!imovelRepository.existsByIdAndAtivo(idImovel, true)){
            throw new ExceptionStatus404(String.format("Nenhum(a) Imovel com Id com o valor '%d' foi encontrado.", idImovel));
        }
        return imovelRepository.findByIdEqualsAndAtivo(idImovel, true);
    }

    public void excluirImovel(Long idImovel) throws Exception {
       Imovel imovel = buscarImovelPorId(idImovel);
       if(existeAnuncioService.existeAnuncioDesteImovel(imovel)){
           throw new ExceptionStatus400("Não é possível excluir um imóvel que possua um anúncio.");
       }
       else {
           imovel.setAtivo(false);
       }
        imovelRepository.save(imovel);
    }
}
