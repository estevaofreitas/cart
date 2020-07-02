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

import br.com.mateus.cart.persistence.entity.Produto;
import br.com.mateus.cart.persistence.repository.ProdutoRepository;
import io.quarkus.panache.common.Sort;

@Path("produto")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class ProdutoEndpoint {

	@Inject
	ProdutoRepository produtoRepository;

	@GET
	public List<Produto> get() {
		return produtoRepository.listAll(Sort.by("descricao"));
	}

	@GET
	@Path("{id}")
	public Produto get(@PathParam("id") Long id) {
		Produto entity = produtoRepository.findById(id);
		if (entity == null) {
			throw new WebApplicationException("Produto com id " + id + " não pode ser encontrado.", Status.NOT_FOUND);
		}
		return entity;
	}

	@GET
	@Path("/buscar/{descricao}")
	public List<Produto> get(@PathParam("descricao") String descricao) {
		if (Strings.isNullOrEmpty(descricao)) {
			throw new WebApplicationException("Atributo \"descricao\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		List<Produto> entities = produtoRepository.findByDescricao(descricao);

		if (entities == null || entities.size() == 0) {
			throw new WebApplicationException("Produto com descricao " + descricao + " não pode ser encontrado.",
					Status.NOT_FOUND);
		}
		return entities;
	}

	@POST
	@Transactional
	public Response create(Produto produto) {
		if (produto.getId() != null) {
			throw new WebApplicationException("Atributo \"id\" não é válido nesta requisição.", Status.BAD_REQUEST);
		}
		
		validate(produto);

		produtoRepository.persist(produto);
		return Response.ok(produto).status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public Produto update(@PathParam("id") Long id, Produto produto) {
		validate(produto);

		Produto entity = produtoRepository.findById(id);

		if (entity == null) {
			throw new WebApplicationException("Produto com id " + id + " não pode ser encontrado.", Status.NOT_FOUND);
		}

		entity.setDescricao(produto.getDescricao());
		entity.setPreco(produto.getPreco());
		entity.setUnidade(produto.getUnidade());
		return entity;
	}

	private void validate(Produto produto) {
		if (Strings.isNullOrEmpty(produto.getDescricao())) {
			throw new WebApplicationException("Atributo \"descricao\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		if (produto.getPreco() == null) {
			throw new WebApplicationException("Atributo \"preco\" não foi encontrado no request.", Status.BAD_REQUEST);
		}

		if (Strings.isNullOrEmpty(produto.getUnidade())) {
			throw new WebApplicationException("Atributo \"descricao\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}
	}
	
	@DELETE
	@Path("{id}")
	@Transactional
	public Response delete(@PathParam("id") Long id) {
		Produto entity = produtoRepository.findById(id);
		if (entity == null) {
			throw new WebApplicationException("Produto com id " + id + " não pode ser encontrado.", Status.NOT_FOUND);
		}
		produtoRepository.delete(entity);
		return Response.status(Status.ACCEPTED).build();
	}
}