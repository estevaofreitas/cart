INSERT INTO endereco (bairro, cep, estado, logradouro, municipio, tipo) VALUES('Cohama', '65.073-780', 'MA', 'Avenida Daniel de la Touche 73-A', 'São Luís', 'COMERCIAL');
INSERT INTO endereco (bairro, cep, estado, logradouro, municipio, tipo) VALUES('Alemanha', '65.055-394', 'MA', 'Av. Dois, 10 - G.C Q.153', 'São Luís', 'COMERCIAL');
INSERT INTO endereco (bairro, cep, estado, logradouro, municipio, tipo) VALUES('Anil', '65.095-602', 'MA', 'Av. Eng. Emiliano Macieira, 215', 'São Luís', 'COMERCIAL');
INSERT INTO endereco (bairro, cep, estado, logradouro, municipio, tipo) VALUES('Forquilha', '65.054-005', 'MA', 'Estrada de Ribamar Km3', 'São Luís', 'RESIDENCIAL');

INSERT INTO pessoa (cpfcnpj, nome, telefone, tipo, endereco_id) VALUES('03.995.515/0013-09', 'Mateus Supermercados S.A.', '(98) 3542-9000', 1, 1);
INSERT INTO pessoa (cpfcnpj, nome, telefone, tipo, endereco_id) VALUES('01.225.335/0015-03', 'Rodovel Transportes', '(98) 3245-1165', 1, 2);
INSERT INTO pessoa (cpfcnpj, nome, telefone, tipo, endereco_id) VALUES('01.115.115/0012-01', 'Meta Transportes', '(98) 3241-1997', 1, 3);
INSERT INTO pessoa (cpfcnpj, nome, telefone, tipo, endereco_id) VALUES('984.913.853-04', 'Estevão Góes', '(99) 98839-0678', 0, 3);

INSERT INTO public.opcaofrete (descricao, prazo, preco, transportador_id) VALUES('Express', 4, 40.45, 2);
INSERT INTO public.opcaofrete (descricao, prazo, preco, transportador_id) VALUES('Ultra', 1, 80.45, 2);
INSERT INTO public.opcaofrete (descricao, prazo, preco, transportador_id) VALUES('Normal', 2, 130.45, 3);

INSERT INTO produto(descricao, preco, unidade) VALUES ('Laranja', 2.20, 'KG');
INSERT INTO produto(descricao, preco, unidade) VALUES ('Maça', 4.25, 'KG');
INSERT INTO produto(descricao, preco, unidade) VALUES ('Banana', 3.25, 'KG');

INSERT INTO public.operacao ("data", tipo, valortotal, valortotalfrete, valortotalprodutos, destinatario_id, enderecoentrega_id, opcaofrete_id) 
      VALUES('01/07/2020', 'VENDA', 62.45, 40.45, 22, 4, 4, 1);
INSERT INTO public.itemoperacao (quantidade, operacao_id, produto_id, valor) VALUES(10, 1, 1, 22);
