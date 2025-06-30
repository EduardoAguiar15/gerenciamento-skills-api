package br.com.neki.dtos;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UsuarioInserirDTO {

	@NotBlank(message = "O email não pode ser vazio.")
	@Email(message = "Formato de email inválido.")
	private String email;

	@NotBlank(message = "A senha não pode ser vazia.")
	@Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
	private String senha;

	@NotBlank(message = "Confirma senha não pode ser vazio.")
	@Size(min = 6, message = "Confirma senha deve ter pelo menos 6 caracteres.")
	private String confirmaSenha;

	private String recaptchaToken;

	private LocalDateTime dataCadastro;

	public UsuarioInserirDTO() {

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

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getRecaptchaToken() {
		return recaptchaToken;
	}

	public void setRecaptchaToken(String recaptchaToken) {
		this.recaptchaToken = recaptchaToken;
	}
}