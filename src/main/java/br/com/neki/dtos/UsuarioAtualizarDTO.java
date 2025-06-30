package br.com.neki.dtos;

public class UsuarioAtualizarDTO {

	private String email;

	private String senhaAtual;

	private String novaSenha;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
}