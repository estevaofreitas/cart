package br.com.mateus.cart.persistence.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.mateus.cart.persistence.entity.Pessoa;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PessoaRepository implements PanacheRepository<Pessoa> {

	public List<Pessoa> findByNome(String nome) {
		return find("lower(nome) like lower( ?1 )", "%" + nome + "%").list();
	}

}
