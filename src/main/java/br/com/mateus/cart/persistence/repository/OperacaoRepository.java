package br.com.mateus.cart.persistence.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.mateus.cart.persistence.entity.Operacao;
import br.com.mateus.cart.persistence.entity.Operacao.TipoOperacao;
import br.com.mateus.cart.persistence.entity.Pessoa;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class OperacaoRepository implements PanacheRepository<Operacao> {

	public List<Operacao> findByVendaDestinatario(Pessoa pessoa) {
		return find("destinatario = ?1 and tipo = ?2 order by data desc", pessoa, TipoOperacao.VENDA).list();
	}
}
