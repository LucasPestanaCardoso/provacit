package br.com.service;

import br.com.enums.StatusUploadEnum;
import br.com.enums.TipoImagemEnum;
import br.com.exception.BusinessException;
import br.com.model.Imagem;
import br.com.model.Usuario;
import br.com.prova.ProvaApplication;
import br.com.repository.ImagemRepository;
import br.com.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProvaApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class ImagemServiceTest {

    @Autowired
    private  ImagemService imagemService;

    @Autowired
    private  ImagemRepository repository;

    @Autowired
    private  UsuarioRepository usuarioRepository;

    private MockMultipartFile file;

    @Before
    public void configTests() {
        usuarioRepository.save(Usuario.builder().id("99").nome("Teste").build());
        byte[] bytes = new byte[1024];
        file = new MockMultipartFile(
                        "file",
                        "arquivoJunitParaTeste.png",
                        MediaType.APPLICATION_OCTET_STREAM_VALUE,
                        bytes);
    }

    @After
    public void destroy() {
        usuarioRepository.deleteById("99");
        Imagem imagem = repository.findByNome("arquivoJunitParaTeste");
        if(imagem != null) {
            repository.deleteById(imagem.getId());
        }
    }

    @Test
    public void validaTamanhoArquivoTest() throws BusinessException, IOException {
        byte[] bytes = new byte[1024 * 1024 * 10];

        file = new MockMultipartFile(
                "file",
                "arquivoJunitParaTeste.png",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                bytes);

        BusinessException businessException =  Assertions.assertThrows(BusinessException.class , () -> {
            imagemService.salvarImagem(file , "99");
        });

        String expectedMessage = "Tamanho maximo de 5MB excedido";
        Assert.assertTrue(expectedMessage.equals(businessException.getMessage()));
    }

    @Test
    public void validaTipoDoArquivoTest() throws BusinessException, IOException {
        byte[] bytes = new byte[1024];
        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "arquivoJunitParaTeste.pdf",
                        MediaType.APPLICATION_OCTET_STREAM_VALUE,
                        bytes);

        BusinessException businessException =  Assertions.assertThrows(BusinessException.class , () -> {
            imagemService.salvarImagem(file , "99");
        });

        String expectedMessage = "Extensao nao permitida";
        Assert.assertTrue(expectedMessage.equals(businessException.getMessage()));
    }

    @Test
    public void validaUsuarioExistenteTest() throws BusinessException, IOException {
        BusinessException businessException =  Assertions.assertThrows(BusinessException.class , () -> {
            imagemService.salvarImagem(file , "555");
        });

        String expectedMessage = "Usuario nao encontrado";
        Assert.assertTrue(expectedMessage.equals(businessException.getMessage()));
    }


    @Test
    public void validaUsuarioObrigatorioTest() throws BusinessException, IOException {
        BusinessException businessException =  Assertions.assertThrows(BusinessException.class , () -> {
            imagemService.salvarImagem(file , "");
        });

        String expectedMessage = "ID do Usuario obrigatorio";
        Assert.assertTrue(expectedMessage.equals(businessException.getMessage()));
    }


    @Test
    public void salvarImagemTest() throws BusinessException, IOException {
        imagemService.salvarImagem(file , "99");

        Imagem imagem = repository.findByNome("arquivoJunitParaTeste");
        Assert.assertTrue(imagem != null);
    }

    @Test
    public void validaImagemDuplicadaTest() throws BusinessException, IOException {
        BusinessException businessException =  Assertions.assertThrows(BusinessException.class , () -> {
            imagemService.salvarImagem(file , "99");
            imagemService.salvarImagem(file , "99");
        });

        String expectedMessage = "Ja existe um arquivo com este nome, favor alterar";
        Assert.assertTrue(expectedMessage.equals(businessException.getMessage()));
    }


    @Test
    public void carregarImagemTest() throws BusinessException, IOException {
        imagemService.salvarImagem(file , "99");
        Imagem imagem = imagemService.carregarImagem("arquivoJunitParaTeste");
        Assert.assertTrue(imagem != null);
    }

    @Test
    public void validaImagemNaoEncontradaTest() throws BusinessException {
        BusinessException businessException =  Assertions.assertThrows(BusinessException.class , () -> {
            imagemService.carregarImagem("arquivoJunitParaTeste");
        });

        String expectedMessage = "Imagem nao encontrada";
        Assert.assertTrue(expectedMessage.equals(businessException.getMessage()));
    }

    @Test()
    public void carregarImagensTest() throws BusinessException, IOException {
        imagemService.salvarImagem(file , "99");
        List<Imagem> imagens = imagemService.carregarImagens("");
        Assert.assertTrue(imagens instanceof Collection<?>);
    }

    @Test()
    public void carregarImagens2Test() throws BusinessException, IOException {
        imagemService.salvarImagem(file , "99");
        List<Imagem> imagens = imagemService.carregarImagens("arquivoJunitParaTeste");
        Assert.assertTrue(imagens instanceof Collection<?>);
    }

    @Test()
    public void deletarImagensTest() throws BusinessException, IOException {
        imagemService.salvarImagem(file , "99");
        imagemService.deletarImagens("99");

        List<Imagem> imagem = repository.findByUsuario(Usuario.builder().id("99").build());
        Assert.assertTrue(CollectionUtils.isEmpty(imagem));
    }





}
