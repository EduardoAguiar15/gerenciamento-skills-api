package br.com.neki.dtos;

import java.time.LocalDateTime;

import br.com.neki.model.Usuario;

public class UsuarioDTO {

	private Long id;

	private String email;

	private LocalDateTime dataCadastro;

	public UsuarioDTO() {

	}

	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.email = usuario.getEmail();
		this.dataCadastro = usuario.getDataCadastro();
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

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
}