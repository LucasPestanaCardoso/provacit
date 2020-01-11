package br.com.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusUploadEnum {

    EM_ANDAMENTO("Em andamento"),
    FALHA("Falha"),
    CONCLUIDO("Concluído");

    private String status;
}
