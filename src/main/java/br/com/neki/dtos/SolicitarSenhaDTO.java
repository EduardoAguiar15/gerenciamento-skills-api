package br.com.neki.dtos;

import java.time.LocalDateTime;

public class SolicitarSenhaDTO {
	private String email;
	private String recaptchaToken;
	private LocalDateTime expiraEm;
	private LocalDateTime servidorAgora;

	public SolicitarSenhaDTO(String email, LocalDateTime expiraEm, LocalDateTime servidorAgora) {
		this.email = email;
		this.expiraEm = expiraEm;
		this.servidorAgora = servidorAgora;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRecaptchaToken() {
		return recaptchaToken;
	}

	public void setRecaptchaToken(String recaptchaToken) {
		this.recaptchaToken = recaptchaToken;
	}

	public LocalDateTime getExpiraEm() {
		return expiraEm;
	}

	public void setExpiraEm(LocalDateTime expiraEm) {
		this.expiraEm = expiraEm;
	}

	public LocalDateTime getServidorAgora() {
		return servidorAgora;
	}

	public void setServidorAgora(LocalDateTime servidorAgora) {
		this.servidorAgora = servidorAgora;
	}
}