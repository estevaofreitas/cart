package br.com.mateus.cart.persistence.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.mateus.cart.persistence.entity.OpcaoFrete;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class OpcaoFreteRepository implements PanacheRepository<OpcaoFrete> {

}
