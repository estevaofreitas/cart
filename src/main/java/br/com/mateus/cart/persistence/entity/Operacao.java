package br.com.mateus.cart.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;

@Entity
public class Operacao {

	public enum TipoOperacao {
		VENDA, DEVOLUCAO;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoOperacao tipo;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Endereco enderecoEntrega;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private OpcaoFrete opcaoFrete;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Pessoa destinatario;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@OneToMany(mappedBy = "operacao")
	private List<ItemOperacao> itens;

	@Column(nullable = false, precision = 7, scale = 2)
	@Digits(integer = 9, fraction = 2)
	private BigDecimal valorTotal;

	@Column(nullable = false, precision = 7, scale = 2)
	@Digits(integer = 9, fraction = 2)
	private BigDecimal valorTotalProdutos;

	@Column(nullable = false, precision = 7, scale = 2)
	@Digits(integer = 9, fraction = 2)
	private BigDecimal valorTotalFrete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoOperacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoOperacao tipo) {
		this.tipo = tipo;
	}

	public Endereco getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public OpcaoFrete getOpcaoFrete() {
		return opcaoFrete;
	}

	public void setOpcaoFrete(OpcaoFrete opcaoFrete) {
		this.opcaoFrete = opcaoFrete;
	}

	public void setEnderecoEntrega(Endereco enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public Pessoa getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Pessoa destinatario) {
		this.destinatario = destinatario;
	}

	public List<ItemOperacao> getItens() {
		return itens;
	}

	public void setItens(List<ItemOperacao> itens) {
		this.itens = itens;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorTotalProdutos() {
		return valorTotalProdutos;
	}

	public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
		this.valorTotalProdutos = valorTotalProdutos;
	}

	public BigDecimal getValorTotalFrete() {
		return valorTotalFrete;
	}

	public void setValorTotalFrete(BigDecimal valorTotalFrete) {
		this.valorTotalFrete = valorTotalFrete;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
