package br.com.repository;

import br.com.model.Imagem;
import br.com.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagemRepository extends MongoRepository<Imagem , String> {

    Imagem findByNome(String nome);

    List<Imagem> findByUsuario(Usuario usuario);

}
