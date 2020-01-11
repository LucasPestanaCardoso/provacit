package br.com.prova;

import br.com.model.Imagem;
import br.com.model.Usuario;
import br.com.repository.ImagemRepository;
import br.com.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({"br.com"})
@EnableSwagger2
@EnableMongoRepositories(basePackages = "br.com.repository")
public class ProvaApplication implements CommandLineRunner {

    public static void main(String[] args) {

        SpringApplication.run(ProvaApplication.class, args);
    }

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.deleteAll();
        repository.save(Usuario.builder().id("1").nome("Lucas Pestana").build());
        repository.save(Usuario.builder().id("2").nome("Wilker").build());
        repository.save(Usuario.builder().id("3").nome("Thomas").build());
    }
}
