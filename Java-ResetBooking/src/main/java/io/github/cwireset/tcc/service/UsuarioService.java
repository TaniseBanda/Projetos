package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.ExceptionStatus400;
import io.github.cwireset.tcc.exception.ExceptionStatus404;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CaminhoAvatar caminhoAvatar;

    public Usuario cadastrarUsuario(Usuario usuario) throws Exception {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ExceptionStatus400(String.format("Já existe um recurso do tipo Usuario com E-Mail com o valor '%s'.", usuario.getEmail()));
        }
        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new ExceptionStatus400(String.format("Já existe um recurso do tipo Usuario com CPF com o valor '%s'.", usuario.getCpf()));
        }
        usuario.setAvatar(caminhoAvatar.getAvatar());

        return usuarioRepository.save(usuario);
    }

    public Page<Usuario> listarUsuarios(Pageable pageable) {
        //Pageable ordenarPorNome = PageRequest.of(0, 10, Sort.by("nome"));
        return usuarioRepository.findAll(pageable);
    }

    public Usuario buscarUsuarioPorId(Long idUsuario) throws Exception {
        if(!usuarioRepository.existsById(idUsuario)){
            throw new ExceptionStatus404(String.format("Nenhum(a) Usuario com Id com o valor '%d' foi encontrado.", idUsuario));
        }

        return usuarioRepository.findByIdEquals(idUsuario);
    }

    public Usuario buscarUsuarioPorCpf(String cpf) throws Exception {
        if(!usuarioRepository.existsByCpf(cpf)){
            throw new ExceptionStatus404(String.format("Nenhum(a) Usuario com CPF com o valor '%s' foi encontrado.", cpf));
        }

        return usuarioRepository.findByCpfEquals(cpf);
    }

    public Usuario alterarUsuario(Long id, Usuario usuario) throws Exception {

        Usuario usuarioBd = buscarUsuarioPorId(id);

        usuarioBd.setNome(usuario.getNome());
        if (!usuarioBd.getEmail().equalsIgnoreCase(usuario.getEmail()) && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ExceptionStatus400(String.format("Já existe um recurso do tipo Usuario com E-Mail com o valor '%s'.", usuario.getEmail()));
        }
        usuarioBd.setEmail(usuario.getEmail());
        usuarioBd.setSenha(usuario.getSenha());
        usuarioBd.setDataNascimento(usuario.getDataNascimento());
        if (usuario.getEndereco() != null) {
            if (usuarioBd.getEndereco() == null) {
                usuarioBd.setEndereco(usuario.getEndereco());
            } else {
                usuarioBd.getEndereco().setBairro(usuario.getEndereco().getBairro());
                usuarioBd.getEndereco().setCep(usuario.getEndereco().getCep());
                usuarioBd.getEndereco().setCidade(usuario.getEndereco().getCidade());
                usuarioBd.getEndereco().setComplemento(usuario.getEndereco().getComplemento());
                usuarioBd.getEndereco().setEstado(usuario.getEndereco().getEstado());
                usuarioBd.getEndereco().setLogradouro(usuario.getEndereco().getLogradouro());
                usuarioBd.getEndereco().setNumero(usuario.getEndereco().getNumero());
            }
        }

        return usuarioRepository.save(usuarioBd);
    }

}
