package br.com.neki.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RelacionamentoVincularDTO {

	private Long usuarioId;

	@NotNull(message = "O ID da skill não pode ser nulo")
	private Long skillId;

	@Min(value = 0, message = "O level não pode ser menor que 0.")
	@Max(value = 10, message = "O level não pode ser maior que 10.")
	private Integer level;

	public RelacionamentoVincularDTO() {
	}

	public RelacionamentoVincularDTO(Long usuarioId, Long skillId, Integer level) {
		this.usuarioId = usuarioId;
		this.skillId = skillId;
		this.level = level;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Long getSkillId() {
		return skillId;
	}

	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}