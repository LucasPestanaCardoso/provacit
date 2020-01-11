package br.com.controller;

import br.com.enums.StatusUploadEnum;
import br.com.enums.TipoImagemEnum;
import br.com.exception.BusinessException;
import br.com.model.Imagem;
import br.com.model.Usuario;
import br.com.prova.ProvaApplication;
import br.com.repository.UsuarioRepository;
import br.com.service.ImagemService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ProvaApplication.class)
@AutoConfigureMockMvc
public class ImagemControllerTest {

    @InjectMocks
    private ImagemController imagemController;

    @Mock
    ImagemService service;

    @Autowired
    private  UsuarioRepository usuarioRepository;

    private MockMultipartFile file;
    private Usuario usuario;
    private Imagem imagem;


    @Before
    public void configTests() {
         usuario = Usuario.builder().id("99").nome("Teste").build();
         imagem = Imagem.builder()
                .nome("teste")
                .status(StatusUploadEnum.CONCLUIDO)
                .tipo(TipoImagemEnum.JPEG)
                .usuario(usuario).build();

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
    }

    @Test
    public void uploadImagemTest() throws BusinessException, IOException {
        Mockito.doNothing().when(service).salvarImagem(file, "99");
        ResponseEntity<?> responseEntity = imagemController.uploadImagem(file , "99");
        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    public void deleteTest() throws BusinessException {
        Mockito.doNothing().when(service).deletarImagens("99");
        ResponseEntity<?> responseEntity = imagemController.delete( "99");
        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));
    }


    @Test
    public void downloadTest() throws BusinessException {

        Mockito.when(service.carregarImagem("teste")).thenReturn(imagem);

        ResponseEntity<?> responseEntity = imagemController.download("teste");
        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    public void  listarImagemTest() throws BusinessException {

        Mockito.when(service.carregarImagens("teste")).thenReturn(newArrayList(imagem));

        ResponseEntity<?> responseEntity = imagemController.listarImagem("teste");
        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));
    }






}
