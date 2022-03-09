package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExisteAnuncioService {
    @Autowired
    private AnuncioRepository anuncioRepository;

    public ExisteAnuncioService(AnuncioRepository anuncioRepository) {
        this.anuncioRepository = anuncioRepository;
    }

    public boolean existeAnuncioDesteImovel(Imovel imovel) {
        return anuncioRepository.existsByImovelAndAtivo(imovel, true);
    }

}
