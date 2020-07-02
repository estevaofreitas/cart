package br.com.mateus.cart.endpoints;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.base.Strings;

import br.com.mateus.cart.persistence.entity.OpcaoFrete;
import br.com.mateus.cart.persistence.entity.Pessoa;
import br.com.mateus.cart.persistence.repository.OpcaoFreteRepository;
import br.com.mateus.cart.persistence.repository.PessoaRepository;
import io.quarkus.panache.common.Sort;

@Path("frete")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class OpcaoFreteEndpoint {

	@Inject
	OpcaoFreteRepository opcaoFreteRepository;

	@Inject
	PessoaRepository pessoaRepository;

	@GET
	public List<OpcaoFrete> get() {
		return opcaoFreteRepository.listAll(Sort.by("prazo"));
	}

	@GET
	@Path("{id}")
	public OpcaoFrete get(@PathParam("id") Long id) {
		OpcaoFrete entity = opcaoFreteRepository.findById(id);
		if (entity == null) {
			throw new WebApplicationException("Opção de Frete com id " + id + " não pode ser encontrada.",
					Status.NOT_FOUND);
		}
		return entity;
	}

	@POST
	@Transactional
	public Response create(OpcaoFrete opcaoFrete) {
		if (opcaoFrete.getId() != null) {
			throw new WebApplicationException("Atributo \"id\" não é válido nesta requisição.", Status.BAD_REQUEST);
		}

		validate(opcaoFrete);

		Pessoa transportador = pessoaRepository.findById(opcaoFrete.getTransportador().getId());
		opcaoFrete.setTransportador(transportador);
		opcaoFreteRepository.persist(opcaoFrete);

		return Response.ok(opcaoFrete).status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public OpcaoFrete update(@PathParam("id") Long id, OpcaoFrete opcaoFrete) {
		validate(opcaoFrete);

		OpcaoFrete entity = opcaoFreteRepository.findById(id);

		if (entity == null) {
			throw new WebApplicationException("Opção de Frete com id " + id + " não pode ser encontrada.",
					Status.NOT_FOUND);
		}

		Pessoa transportador = pessoaRepository.findById(opcaoFrete.getTransportador().getId());
		entity.setTransportador(transportador);
		entity.setDescricao(opcaoFrete.getDescricao());
		entity.setPreco(opcaoFrete.getPreco());
		entity.setPrazo(opcaoFrete.getPrazo());
		opcaoFreteRepository.persist(entity);

		return entity;
	}

	private void validate(OpcaoFrete opcaoFrete) {
		if (opcaoFrete.getTransportador() == null) {
			throw new WebApplicationException("Atributo \"transportador\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		if (opcaoFrete.getTransportador().getId() == null) {
			throw new WebApplicationException("Atributo \"transportador.id\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		if (opcaoFrete.getPreco() == null) {
			throw new WebApplicationException("Atributo \"preco\" não foi encontrado no request.", Status.BAD_REQUEST);
		}

		if (Strings.isNullOrEmpty(opcaoFrete.getDescricao())) {
			throw new WebApplicationException("Atributo \"descricao\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public Response delete(@PathParam("id") Long id) {
		OpcaoFrete entity = opcaoFreteRepository.findById(id);
		if (entity == null) {
			throw new WebApplicationException("Opção de Frete com id " + id + " não pode ser encontrada.",
					Status.NOT_FOUND);
		}
		opcaoFreteRepository.delete(entity);
		return Response.status(Status.ACCEPTED).build();
	}

}