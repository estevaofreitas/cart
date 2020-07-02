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

import br.com.mateus.cart.persistence.entity.Endereco;
import br.com.mateus.cart.persistence.entity.Pessoa;
import br.com.mateus.cart.persistence.repository.EnderecoRepository;
import br.com.mateus.cart.persistence.repository.PessoaRepository;
import io.quarkus.panache.common.Sort;

@Path("pessoa")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class PessoaEndpoint {

	@Inject
	PessoaRepository pessoaRepository;

	@Inject
	EnderecoRepository enderecoRepository;
	
	@GET
	public List<Pessoa> get() {
		return pessoaRepository.listAll(Sort.by("nome"));
	}

	@GET
	@Path("{id}")
	public Pessoa get(@PathParam("id") Long id) {
		Pessoa entity = pessoaRepository.findById(id);
		if (entity == null) {
			throw new WebApplicationException("Pessoa com id " + id + " não pode ser encontrada.", Status.NOT_FOUND);
		}
		return entity;
	}

	@GET
	@Path("/buscar/{nome}")
	public List<Pessoa> get(@PathParam("nome") String nome) {
		if (Strings.isNullOrEmpty(nome)) {
			throw new WebApplicationException("Atributo \"nome\" não foi encontrado no request.", Status.BAD_REQUEST);
		}

		return pessoaRepository.findByNome(nome);
	}

	@POST
	@Transactional
	public Response create(Pessoa pessoa) {
		if (pessoa.getId() != null) {
			throw new WebApplicationException("Atributo \"id\" não é válido nesta requisição.", Status.BAD_REQUEST);
		}
		
		validate(pessoa);
		
		enderecoRepository.persist(pessoa.getEndereco());
		pessoaRepository.persist(pessoa);
		return Response.ok(pessoa).status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public Pessoa update(@PathParam("id") Long id, Pessoa pessoa) {
		validate(pessoa);

		Pessoa entity = pessoaRepository.findById(id);
		
		if (entity == null) {
			throw new WebApplicationException("Pessoa com id " + id + " não pode ser encontrada.", Status.NOT_FOUND);
		}

		Endereco endereco = entity.getEndereco();
		endereco.setBairro(pessoa.getEndereco().getBairro());
		endereco.setCep(pessoa.getEndereco().getCep());
		endereco.setEstado(pessoa.getEndereco().getEstado());
		endereco.setLogradouro(pessoa.getEndereco().getLogradouro());
		endereco.setMunicipio(pessoa.getEndereco().getMunicipio());
		endereco.setTipo(pessoa.getEndereco().getTipo());		
		enderecoRepository.persist(endereco);
		
		entity.setNome(pessoa.getNome());
		entity.setCpfCnpj(pessoa.getCpfCnpj());
		entity.setTelefone(pessoa.getTelefone());		
		entity.setEndereco(endereco);		
		pessoaRepository.persist(pessoa);

		return entity;
	}
	
	private void validate(Pessoa pessoa) {
		if (Strings.isNullOrEmpty(pessoa.getNome())) {
			throw new WebApplicationException("Atributo \"nome\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
		if (Strings.isNullOrEmpty(pessoa.getCpfCnpj())) {
			throw new WebApplicationException("Atributo \"cpfcnpj\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
		if (Strings.isNullOrEmpty(pessoa.getTelefone())) {
			throw new WebApplicationException("Atributo \"telefone\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
		if (pessoa.getTipo() != null) {
			throw new WebApplicationException("Atributo \"tipo\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
		if (pessoa.getEndereco() != null) {
			throw new WebApplicationException("Atributo \"endereco\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
				
		if (Strings.isNullOrEmpty(pessoa.getEndereco().getBairro())) {
			throw new WebApplicationException("Atributo \"endereco.bairro\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
		if (Strings.isNullOrEmpty(pessoa.getEndereco().getCep())) {
			throw new WebApplicationException("Atributo \"endereco.cep\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
		if (Strings.isNullOrEmpty(pessoa.getEndereco().getEstado())) {
			throw new WebApplicationException("Atributo \"endereco.bairro\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
		if (Strings.isNullOrEmpty(pessoa.getEndereco().getLogradouro())) {
			throw new WebApplicationException("Atributo \"endereco.logradouro\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
		if (Strings.isNullOrEmpty(pessoa.getEndereco().getMunicipio())) {
			throw new WebApplicationException("Atributo \"endereco.municipio\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
		if (pessoa.getEndereco().getTipo() == null) {
			throw new WebApplicationException("Atributo \"endereco.tipo\" não foi encontrado no request.", Status.BAD_REQUEST);
		}
		
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public Response delete(@PathParam("id") Long id) {
		Pessoa entity = pessoaRepository.findById(id);
		if (entity == null) {
			throw new WebApplicationException("Pessoa com id " + id + " não pode ser encontrada.", Status.NOT_FOUND);
		}
		pessoaRepository.delete(entity);
		return Response.status(Status.ACCEPTED).build();
	}

}