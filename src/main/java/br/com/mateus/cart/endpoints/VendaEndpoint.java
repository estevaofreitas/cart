package br.com.mateus.cart.endpoints;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import br.com.mateus.cart.persistence.entity.ItemOperacao;
import br.com.mateus.cart.persistence.entity.OpcaoFrete;
import br.com.mateus.cart.persistence.entity.Operacao;
import br.com.mateus.cart.persistence.entity.Operacao.TipoOperacao;
import br.com.mateus.cart.persistence.entity.Pessoa;
import br.com.mateus.cart.persistence.entity.Produto;
import br.com.mateus.cart.persistence.repository.OpcaoFreteRepository;
import br.com.mateus.cart.persistence.repository.OperacaoRepository;
import br.com.mateus.cart.persistence.repository.PessoaRepository;
import br.com.mateus.cart.persistence.repository.ProdutoRepository;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("venda")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VendaEndpoint {

	@Inject
	OperacaoRepository operacaoRepository;

	@Inject
	OpcaoFreteRepository opcaoFreteRepository;

	@Inject
	PessoaRepository pessoaRepository;

	@Inject
	ProdutoRepository produtoRepository;

	@Inject
	Template nfce;

	@GET
	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "A Nota Fiscal da Venda em formato html", content = @Content(mediaType = "application/json")) })
	@Operation(summary = "Monta a nota para impressão", description = "Lista de Notas Fiscais emitidas")
	public List<Operacao> get() {
		return operacaoRepository.listAll(Sort.descending("data"));
	}

	@GET
	@Path("/nota/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_HTML)
	@APIResponses(value = {
			@APIResponse(responseCode = "404", description = "A Venda não foi encontrada", content = @Content(mediaType = "text/plain")),
			@APIResponse(responseCode = "200", description = "A Nota Fiscal da Venda em formato html", content = @Content(mediaType = "application/html")) })
	@Operation(summary = "Monta a nota para impressão", description = "Monta a Nota Fiscal em formato html a partir da Operação de Venda")
	public TemplateInstance emitir(
			@Parameter(description = "ID da Venda concluída", required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("id") Long id) {
		Operacao operacao = operacaoRepository.findById(id);
		if (operacao == null) {
			throw new WebApplicationException("Operação com id " + id + " não pode ser encontrada.", Status.NOT_FOUND);
		}
		Pessoa pessoa = pessoaRepository.findById(1L);

		return nfce.data("vendedor", pessoa).data("operacao", operacao);
	}

	@GET
	@Path("/buscar/{cliente}")
	@APIResponses(value = {
			@APIResponse(responseCode = "404", description = "O Cliente não foi encontrado", content = @Content(mediaType = "text/plain")),
			@APIResponse(responseCode = "200", description = "Lista com todas as Vendas do Cliente", content = @Content(mediaType = "application/json")) })
	@Operation(summary = "Retorna as Vendas de um determinado Cliente", description = "Retorna as Vendas de um determinado Cliente ordenadas a partir da mais recente")
	public List<Operacao> get(
			@Parameter(description = "ID do Cliente", required = true, example = "4", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("cliente") Long id) {
		Pessoa pessoa = pessoaRepository.findById(id);
		if (pessoa == null) {
			throw new WebApplicationException("Pessoa com id " + id + " não pode ser encontrada.", Status.NOT_FOUND);
		}

		return operacaoRepository.findByVendaDestinatario(pessoa);
	}

	@POST
	@Path("/simular")
	@APIResponses(value = {
			@APIResponse(responseCode = "400", description = "Erro de validade dos valores da Venda", content = @Content(mediaType = "text/plain")),
			@APIResponse(responseCode = "200", description = "A operação de venda totalmente calculada", content = @Content(mediaType = "application/json")) })
	@Operation(summary = "Simula os valores totais da venda", description = "Simula utilizando o mesmo código que será usado no fechamento da Venda")
	public Response simular(Operacao operacao) {
		validar(operacao);
		calcular(operacao);

		return Response.ok(operacao).status(Status.CREATED).build();
	}

	@POST
	@Path("/checkout")
	@APIResponses(value = {
			@APIResponse(responseCode = "400", description = "Erro de validade dos valores da Venda", content = @Content(mediaType = "text/plain")),
			@APIResponse(responseCode = "200", description = "A operação de venda totalmente calculada", content = @Content(mediaType = "application/json")) })
	@Operation(summary = "Realiza o fechamento da venda", description = "Realiza o fechamento da Venda")
	public Response checkout(Operacao operacao) {
		validar(operacao);
		calcular(operacao);

		Pessoa pessoa = pessoaRepository.findById(operacao.getDestinatario().getId());
		operacao.setDestinatario(pessoa);
		operacao.setEnderecoEntrega(pessoa.getEndereco());
		operacao.setTipo(TipoOperacao.VENDA);
		operacaoRepository.persist(operacao);

		return Response.ok(operacao).status(Status.CREATED).build();
	}

	private void calcular(Operacao operacao) {
		OpcaoFrete frete = opcaoFreteRepository.findById(operacao.getOpcaoFrete().getId());
		operacao.setOpcaoFrete(frete);
		operacao.setValorTotalFrete(frete.getPreco());

		BigDecimal totalProdutos = BigDecimal.ZERO;
		for (ItemOperacao item : operacao.getItens()) {
			Produto produto = produtoRepository.findById(item.getId());
			item.setValor(produto.getPreco().multiply(item.getQuantidade()));
			totalProdutos = totalProdutos.add(item.getValor());
		}
		operacao.setValorTotalProdutos(totalProdutos);

		operacao.setValorTotal(operacao.getValorTotalFrete().add(operacao.getValorTotalProdutos()));
	}

	private void validar(Operacao operacao) {
		if (operacao.getDestinatario() == null) {
			throw new WebApplicationException("Atributo \"destinatario\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		if (operacao.getDestinatario().getId() == null) {
			throw new WebApplicationException("Atributo \"destinatario.id\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		if (operacao.getEnderecoEntrega() == null) {
			throw new WebApplicationException("Atributo \"enderecoEntrega\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		if (operacao.getEnderecoEntrega().getId() == null) {
			throw new WebApplicationException("Atributo \"enderecoEntrega.id\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		if (operacao.getOpcaoFrete() == null) {
			throw new WebApplicationException("Atributo \"opcaoFrete\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		if (operacao.getOpcaoFrete().getId() == null) {
			throw new WebApplicationException("Atributo \"opcaoFrete.id\" não foi encontrado no request.",
					Status.BAD_REQUEST);
		}

		for (ItemOperacao item : operacao.getItens()) {
			if (item.getProduto() == null) {
				throw new WebApplicationException("Atributo \"itemproduto.produto\" não foi encontrado no request.",
						Status.BAD_REQUEST);
			}

			if (item.getProduto().getId() == null) {
				throw new WebApplicationException("Atributo \"itemproduto.produto.id\" não foi encontrado no request.",
						Status.BAD_REQUEST);
			}

			if (item.getQuantidade() == null) {
				throw new WebApplicationException("Atributo \"itemproduto.quantidade\" não foi encontrado no request.",
						Status.BAD_REQUEST);
			}
		}
	}
}