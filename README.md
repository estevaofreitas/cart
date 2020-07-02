# Projeto Cart

Este projeto é uma pequena simulação do que seria um carrinho de compras com apenas as funcionalidade de "Simular" e "Checkout" de carrinho de compras.

Também foram implementados serviços CRUDs para algumas entidades - Pessoa, OpcaoFrete e Produto, desta forma pode-se realizar várias operações de venda e simular alterações no sistema. 

## Testando a aplicação

Conforme solicitado, o arquivo abaixo do Docker Compose pode ser utilizado para subir a aplicação: "https://github.com/estevaofreitas/cart/blob/master/src/main/docker/docker-compose.yml".

Comando:

```
docker-compose up
```

OBS: para facilitar os testes, a imagem do banco é a oficial do PostgreSQL e a cada reinício da aplicação o banco é resetado. 
No entanto, durante o carregamento da aplicação, alguns dados de teste são são carregados em todas as tabelas.

## Testando a aplicação

Para facilitar os testes inclui o app Swagger: "http://localhost:8080/swagger-ui/" , mas caso seja necessário pode-se utilizar o Advanced Rest Client.

Todas as chamadas podem ser testadas diretamente nas duas ferramentas.

Exemplo de conteúdo das chamadas "http://localhost:8080/venda/simular" ou "http://localhost:8080/venda/checkout":

```
{
  "destinatario": {
    "id": 4
  },
  "enderecoEntrega": {
    "id": 4
  },
  "itens": [
    {
      "id": 1,
      "produto": {
        "id": 1
      },
      "quantidade": 10
    }
  ],
  "opcaoFrete": {
    "id": 1
  }
}
```
Existe uma chamada que retorna todas as operações de venda: "http://localhost:8080/venda/".

E outra chamada que retorna as vendas de um cliente: "http://localhost:8080/venda/cliente/4".

A chamada de emissão de nota a partir de uma venda utilizando é a seguinte: "http://localhost:8080/venda/nota/1".

Ela retorna a nota em formato HTML.

Exemplo de inclusão de um CRUD, neste caso é inclusão de Opção de Frete: "http://localhost:8080/frete"

```
{
  "descricao": "Normal",
  "prazo": 1,
  "preco": 200.45,
  "transportador": {
    "id": 3
  }
}
```
Obs: Somente os ids são necessários para estabelecer os relacionamentos dos objetos, desta forma sempre envie os ids como foi feito acima. 
Também não envie os ids dos objetos que ainda serão salvos pois eles são gerados de forma incremental. Isto será rejeitado nas validações.

As outras chamadas - "http://localhost:8080/produto" , "http://localhost:8080/pessoa" , "http://localhost:8080/frete" - foram feitas seguindo as orientações sobre HTTP METHODS:
- POST: incluir, 
- PUT: alterar,
- GET: listar,
- DELETE: deletar
