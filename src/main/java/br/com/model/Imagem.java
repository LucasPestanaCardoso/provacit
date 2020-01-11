package br.com.model;

import br.com.enums.StatusUploadEnum;
import br.com.enums.TipoImagemEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Imagem implements Serializable {

    @Id
    private String id;
    private String nome;
    private byte[] bytes;
    private TipoImagemEnum tipo;
    private StatusUploadEnum status;
    private Usuario usuario;
}
