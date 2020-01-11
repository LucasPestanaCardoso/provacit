package br.com.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TipoImagemEnum {

    JPEG("JPEG", MediaType.IMAGE_JPEG_VALUE),
    JPG("JPG" , MediaType.IMAGE_JPEG_VALUE),
    JFIF("JFIF", MediaType.IMAGE_JPEG_VALUE),
    GIF("GIF", MediaType.IMAGE_GIF_VALUE),
    BMP("BMP", MediaType.MULTIPART_FORM_DATA_VALUE),
    PNG("PNG", MediaType.IMAGE_PNG_VALUE);

    private String nome;
    private String mediaType;

    public static boolean contains(String valor) {
       return Stream.of(TipoImagemEnum.values()).filter(tipo -> tipo.getNome().equalsIgnoreCase(valor)).findAny().isPresent();
    }

    public static TipoImagemEnum getInstance(String valor) {
       return Stream.of(TipoImagemEnum.values()).filter(tipo -> tipo.getNome().equalsIgnoreCase(valor)).findFirst().get();
    }

}
