package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.ExceptionStatus400;
import io.github.cwireset.tcc.exception.ExceptionStatus404;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnuncioService {

    private AnuncioRepository anuncioRepository;
    private ImovelService imovelService;
    private UsuarioService usuarioService;
    private ExisteAnuncioService existeAnuncioService;

    @Autowired
    public AnuncioService(AnuncioRepository anuncioRepository, ImovelService imovelService, UsuarioService usuarioService, ExisteAnuncioService existeAnuncioService) {
        this.anuncioRepository = anuncioRepository;
        this.imovelService = imovelService;
        this.usuarioService = usuarioService;
        this.existeAnuncioService = existeAnuncioService;
    }


    public Anuncio anunciarImovel(CadastrarAnuncioRequest cadastrarAnuncioRequest) throws Exception {
        Imovel imovel = imovelService.buscarImovelPorId(cadastrarAnuncioRequest.getIdImovel());
        Usuario anunciante = usuarioService.buscarUsuarioPorId(cadastrarAnuncioRequest.getIdAnunciante());

        if(existeAnuncioService.existeAnuncioDesteImovel(imovel)){
            throw new ExceptionStatus400(String.format("Já existe um recurso do tipo Anuncio com IdImovel com o valor '%d'.", imovel.getId()));
        }

        Anuncio anuncio = new Anuncio(null,cadastrarAnuncioRequest.getTipoAnuncio(), imovel, anunciante, cadastrarAnuncioRequest.getValorDiaria(), cadastrarAnuncioRequest.getFormasAceitas(), cadastrarAnuncioRequest.getDescricao(), true);

        return anuncioRepository.save(anuncio);
    }

    public Page<Anuncio> listarAnuncios(Pageable pageable) {
        return anuncioRepository.findByAtivo(pageable,true);
    }

    public Page<Anuncio> listarAnunciosPorAnunciante(Pageable pageable, Long idAnunciante) throws Exception {
        Usuario anunciante = usuarioService.buscarUsuarioPorId(idAnunciante);
        return anuncioRepository.findByAnuncianteAndAtivo(pageable, anunciante, true);
    }

    public Anuncio buscarAnuncioPorId (Long idAnuncio) throws Exception {
        if(!anuncioRepository.existsByIdAndAtivo(idAnuncio, true)){
            throw new ExceptionStatus404(String.format("Nenhum(a) Anuncio com Id com o valor '%d' foi encontrado.", idAnuncio));
        }
        return anuncioRepository.findByIdEqualsAndAtivo(idAnuncio, true);
    }

    public void excluirAnuncio(Long idAnuncio) throws Exception {
        Anuncio anuncio = buscarAnuncioPorId(idAnuncio);
        anuncio.setAtivo(false);
        anuncioRepository.save(anuncio);
    }
}
