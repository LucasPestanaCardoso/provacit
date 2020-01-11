package br.com.controller;

import br.com.exception.BusinessException;
import br.com.model.Imagem;
import br.com.service.ImagemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("v1/imagem")
@Slf4j
@Api(value = "Endpoints para gerenciar imagens")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImagemController {

    private final ImagemService service;

    @PostMapping(value = "/upload" ,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Método para fazer upload de imagens")
    public ResponseEntity<?> uploadImagem(
            @ApiParam(value = "Arquivo para Upload",required=true )
            @RequestPart() MultipartFile file,
            @ApiParam(value = "ID do Usuario", required=true )
            @RequestParam() String idUsuario) throws BusinessException, IOException {

        service.salvarImagem(file, idUsuario);
        return new ResponseEntity("Imagem salva com sucesso !!" , HttpStatus.OK);
    }


    @GetMapping(value = "/download" )
    @ApiOperation(value = "Método para realizar o download das imagens")
    public ResponseEntity<?> listar(
            @ApiParam(value = "Nome do Arquivo" , required = true)
            @RequestParam(required = true) String nomeArquivo) throws Exception {

        Imagem imagem = service.carregarImagem(nomeArquivo);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imagem.getTipo().getMediaType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imagem.getNome() + "\"")
                .body(imagem.getBytes());
    }

    @GetMapping(value = "/listagem-imagem" ,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Método para listar as imagens")
    public ResponseEntity<?> listarImagem(
            @ApiParam(value = "Nome do Arquivo", required=false)
            @RequestParam(required=false) String nomeArquivo) throws BusinessException {

       List<Imagem> imgs = service.carregarImagens(nomeArquivo);
       byte[] encoded = Base64.getEncoder().encode(imgs.get(0).getBytes());

        return new ResponseEntity(imgs , HttpStatus.OK);
    }


    @PostMapping(value = "/delete-by-usuario" ,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Método para deletar as imagens pelo Usuario")
    public ResponseEntity<?> delete(
            @ApiParam(value = "ID Usuario", required=true)
            @RequestParam(required=true) String id) throws BusinessException {

        service.deletarImagens(id);
        return new ResponseEntity("Imagens deletadas com sucesso !!" , HttpStatus.OK);
    }

}
