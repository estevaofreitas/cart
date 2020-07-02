package br.com.mateus.cart.persistence.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.mateus.cart.persistence.entity.Produto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {

	public List<Produto> findByDescricao(String descricao) {
		return find("lower(descricao) like lower( ?1 )", "%" + descricao + "%").list();
	}

}
