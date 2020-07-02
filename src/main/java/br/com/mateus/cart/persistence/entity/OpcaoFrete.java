package br.com.mateus.cart.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;

@Entity
@Cacheable
public class OpcaoFrete {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 150, nullable = false)
	private String descricao;

	@ManyToOne(optional = false)
	private Pessoa transportador;

	@Column(nullable = false, precision = 7, scale = 2)
	@Digits(integer = 9, fraction = 2)
	private BigDecimal preco;

	@Column(nullable = false)
	private Integer prazo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Pessoa getTransportador() {
		return transportador;
	}

	public void setTransportador(Pessoa transportador) {
		this.transportador = transportador;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}
}
