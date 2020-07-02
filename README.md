# Projeto Cart

Este projeto é uma pequena simulação do que seria um carrinho de compras com apenas as funcionalidade de "Simular" e "Checkout" de carrinho de compras.

Também foram implementados serviços CRUDs para algumas entidades - Pessoa, OpcaoFrete e Produto, desta forma pode-se realizar várias operações de venda e simular alterações no sistema. 

## Testando a aplicação

Conforme solicitado, o arquivo abaixo do Docker Compose pode ser utilizado para subir a aplicação: https://github.com/estevaofreitas/cart/blob/master/src/main/docker/docker-compose.yml

Comando:

```
docker-compose up
```

## Testando a aplicação

Para facilitar os testes incluir o app Swagger: http://localhost:8080/swagger-ui/

Todas as chamadas podem ser testadas diretamente nele.

Exemplo de conteúdo das chamadas "/venda/simular" ou "/venda/checkout":

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

