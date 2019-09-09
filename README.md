# Cadastro de cervejas artesanais
<br>
<p> Aplicação para cadastro, consulta e manutenção de cervejas artesanais.</p>
<p> Todas as operações são feitas através de requisições em Rest.</p>
<br>
<h4>1 - Tecnologias utilizadas no desenvolvimento:</h4>
<p> Projeto em Spring Boot, utilizando Maven para o controle de dependências.</p>
<p> Para a persistência em banco de dados foi utilizado o Spring Data.</p>
<p> Para o mapeamento de tabelas para classes foi utilizado o JPA.</p>
<h4>2 - Banco de dados:</h4>
<p>O banco de dados utilizado para o teste da aplicação é o MySQL.
Os scripts para criação/exclusão do esquema e tabela está anexado ao projeto, no caminho:
<b>src>>main>>resources>>sql</b>.</p>
<h4>3 - Controller:</h4>
<p>- BeerController: contém as chamadas para todas as requisições em Rest, além da interação com 
o banco de dados por meio da classe de repositório;</p>
<p>- TratamentoExcecaoController: controller para tratamento de retorno de requisições Post, Put e Patch ao
lançar a exceção BeerValidationException, criada para conter informações sobre os campos
da entidade cerveja inválidos ao realizar um cadastro ou atualização.</p>
<h4>4 - Testes unitários:</h4>
<p>Foram criadas duas classes para os testes unitários:</p>
<p>- BeerApplicationTestWithTemplate: classe que utiliza o Rest Template.
Criada para testar requisições POST, PUT, PATCH e DELETE. Foi
utilizado o Mockito para evitar alterações no banco de dados.
Para isso foi aplicado o "Mock" na classe de Repositório BeerRepository;</p>
<p>BeerApplicationTests: classe que utiliza o MockMvc para o teste de todas as requisições
implementadas na aplicação. Nesta classe também foi aplicado o "Mock" em BeerRepository.<p>
<h4>5 - Coleção do Postman</h4>
<p>Foi gerada uma coleção à partir do Postman Client contendo todas as chamadas para requisições
implementadas na aplicação. Essa coleção está em: <b>src>>main>>resources>>postmancollection</b>.</p>
  

    
  
# craftbeer
