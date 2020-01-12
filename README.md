**Subir o Projeto**

Instalar(Raiz do Projeto) -> mvn clean install                                                                            
Rodar os testes(Junit) -> mvn clean test                                                                       
Rodar a aplicação -> mvn spring-boot:run                                                                                         
URL : http://localhost:8080/swagger-ui.html       

**Info's**

- Não pude usar Docker a versão do Win 10 Home não tem suporte 😒

- Além dos métodos pedidos eu criei o `http://localhost:8080/v1/imagem/download` para fazer o download da imagem por que eu fiquei com a dúvida se era para trazer a imagem na API de listagem, então optei por criar um endpoint diferente.

- Na raiz do projeto tem uma pasta chamada e o dump da base do Mongo (db provacit) 
 -  `mongoimport --db provacit --collection usuario --file {seu path}/provacit-json/provacit.usuario.json --jsonArray`
 -  `mongoimport --db provacit --collection imagem --file {seu path}/provacit-json/provacit.imagem.json --jsonArray`

- Utilizei Mockito e fiz Testes com insert no Mongo no Junit também, projeto esta com 90% de Line Coverage no Intellij.

- Já existe 3 Usuários na base ID:1 Lucas Pestana, ID:2 Wilker, ID:3 Thomas.

- Utilizei Lombok caso o projeto apresente erro de compilação na sua IDE so baixar o plugin.

 Qualquer erro que a aplicação apresentar, pode entrar em contato lucaspestanaa@gmail.com

Obrigado !


