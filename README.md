# Projeto Cart

Este projeto é uma pequena simulação do que seria um carrinho de compras com apenas as funcionalidade de "Simular" e "Checkout" de carrinho de compras.

Também foram implementados serviços CRUDs para algumas entidades - Pessoa, OpcaoFrete e Produto, desta forma pode-se realizar várias operações de venda e simular alterações no sistema. 

## Rodando a Aplicação

Conforme solicitado, o arquivo abaixo do Docker Compose pode ser utilizado para subir a aplicação:



Comando:

```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `cart-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/cart-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/cart-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.