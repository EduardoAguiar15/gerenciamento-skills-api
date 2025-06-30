package br.com.neki.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String senha;

	@Column(name = "data_cadastro", nullable = false)
	private LocalDateTime dataCadastro;

	@Column(name = "token_redefinicao_senha")
	private String tokenRedefinicaoSenha;

	@Column(name = "data_expiracao_token")
	private LocalDateTime dataExpiracaoToken;

	public Usuario() {
	}

	public Usuario(Long id, String email, String senha, LocalDateTime dataCadastro) {
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.dataCadastro = dataCadastro;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getTokenRedefinicaoSenha() {
		return tokenRedefinicaoSenha;
	}

	public void setTokenRedefinicaoSenha(String tokenRedefinicaoSenha) {
		this.tokenRedefinicaoSenha = tokenRedefinicaoSenha;
	}

	public LocalDateTime getDataExpiracaoToken() {
		return dataExpiracaoToken;
	}

	public void setDataExpiracaoToken(LocalDateTime dataExpiracaoToken) {
		this.dataExpiracaoToken = dataExpiracaoToken;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataCadastro, email, id, senha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(dataCadastro, other.dataCadastro) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(senha, other.senha);
	}
}