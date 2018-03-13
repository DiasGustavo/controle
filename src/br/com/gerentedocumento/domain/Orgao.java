package br.com.gerentedocumento.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "tbl_orgao")
@NamedQueries({
	@NamedQuery(name = "Orgao.listar", query = "SELECT orgao FROM Orgao orgao"),
	@NamedQuery(name = "Orgao.buscarPorCodigo", query = "SELECT orgao FROM Orgao orgao WHERE orgao.id = :codigo")
})
public class Orgao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cod_orgao")
	private Long id;
	
	@NotEmpty(message = "o campo nome é obrigatório")
	@Size(min = 1, max = 50, message= "Nome deve ter entre 1 e 50 caracteres")
	@Column(name="nome", length=50, nullable=false)
	private String nome;
	
	@NotEmpty(message = "o campo CNPJ é obrigatório")
	@Size(min = 1, max = 18, message= "CNPJ deve ter entre 1 e 18 caracteres")
	@Column(name="cnpj", length=18, nullable=false)
	private String cnpj;
	
	@NotEmpty(message = "o campo repositório é obrigatório")
	@Size(min = 1, max = 200, message= "repositório deve ter entre 1 e 200 caracteres")
	@Column(name="repositorio", length=200, nullable=false)
	private String repositorio;
	
	@NotNull(message = "o campo endereço é obrigatório")
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="endereco_fk", referencedColumnName="cod_endereco", nullable = false)
	private Endereco endereco;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRepositorio() {
		return repositorio;
	}

	public void setRepositorio(String repositorio) {
		this.repositorio = repositorio;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Override
	public String toString() {
		return "Orgao [id=" + id + ", nome=" + nome + ", cnpj=" + cnpj + ", repositorio=" + repositorio + ", brasao="
				+ ", endereco=" + endereco + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Orgao other = (Orgao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
