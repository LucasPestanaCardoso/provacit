package br.com.service;

import br.com.enums.StatusUploadEnum;
import br.com.enums.TipoImagemEnum;
import br.com.exception.BusinessException;
import br.com.model.Imagem;
import br.com.model.Usuario;
import br.com.repository.ImagemRepository;
import br.com.repository.UsuarioRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImagemService {

    //5MB
    private static final int MAX_SIZE_UPLOAD = 5242880;
    private final ImagemRepository imagemRepository;
    private final UsuarioRepository usuarioRepository;

    public void salvarImagem(MultipartFile file, String idUsuario) throws BusinessException, IOException {

        String extensao = validaArquivo(file, idUsuario);

        Imagem img = new Imagem();

         img.setNome(split(file.getOriginalFilename() , ".")[0]);
         img.setBytes(file.getBytes());
         img.setStatus(StatusUploadEnum.CONCLUIDO);
         img.setUsuario(usuarioRepository.findById(idUsuario).get());
         img.setTipo(TipoImagemEnum.getInstance(extensao));

        imagemRepository.save(img);
    }

    @Transactional(readOnly = true)
    public Imagem carregarImagem(String nomeArquivo) throws BusinessException {
        Imagem img = new Imagem();
        if(isNotBlank(nomeArquivo)) {
             nomeArquivo = removeExtension(nomeArquivo);
             img = imagemRepository.findByNome(nomeArquivo);

            if(img == null) {
                log.error("Imagem nao encontrada");
                throw new BusinessException("Imagem nao encontrada");
            }
        }
        return img;
    }

    @Transactional(readOnly = true)
    public List<Imagem> carregarImagens(String nomeArquivo) throws BusinessException {
        if(isNotBlank(nomeArquivo)) {
            return newArrayList(carregarImagem(nomeArquivo));
        } else {
            List<Imagem> imgs =  imagemRepository.findAll();
            if(isEmpty(imgs)) {
                log.error("Nenhuma imagem encontrada");
                throw new BusinessException("Nenhuma imagem encontrada");
            }
            return  imgs;
        }
    }

    private String validaArquivo(MultipartFile file, String idUsuario) throws BusinessException {

        if(file.getSize() > MAX_SIZE_UPLOAD) {
            log.error("Arquivo maior que 5MB");
            throw new BusinessException("Tamanho maximo de 5MB excedido");
        }

        String extensao =  FilenameUtils.getExtension(file.getOriginalFilename());

        if(!TipoImagemEnum.contains(extensao)) {
            log.error("Extensao nao permitida");
            throw new BusinessException("Extensao nao permitida");
        }

        Imagem img = imagemRepository.findByNome(split(file.getOriginalFilename() , ".")[0]);

        if(img != null) {
            log.error("Ja existe um arquivo com este nome, favor alterar");
            throw new BusinessException("Ja existe um arquivo com este nome, favor alterar");
        }

        validaUsuario(idUsuario);
        return extensao;
    }

    private Usuario validaUsuario(String idUsuario) throws BusinessException {
        if(isBlank(idUsuario)) {
            log.error("ID do Usuario obrigatório");
            throw new BusinessException("ID do Usuario obrigatorio");
        }
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if(!usuario.isPresent()) {
            log.error("Usuário não encontrado");
            throw new BusinessException("Usuario nao encontrado");
        }
        return usuario.get();
    }

    public void deletarImagens(String idUsuario) throws BusinessException {
        Usuario usuario = validaUsuario(idUsuario);
        imagemRepository.deleteAll(imagemRepository.findByUsuario(usuario));
    }

}
