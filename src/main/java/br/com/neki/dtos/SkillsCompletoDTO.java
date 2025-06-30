package br.com.neki.dtos;

import br.com.neki.model.Relacionamento;

public class SkillsCompletoDTO {

	private Long skillId;

	private String skillNome;

	private Integer level;

	public SkillsCompletoDTO() {
	}

	public SkillsCompletoDTO(Relacionamento relacionamento) {
		this.skillId = relacionamento.getId().getSkillsId().getId();
		this.skillNome = relacionamento.getId().getSkillsId().getSkillNome();
		this.level = relacionamento.getLevel();
	}

	public Long getSkillId() {
		return skillId;
	}

	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}

	public String getSkillNome() {
		return skillNome;
	}

	public void setSkillNome(String skillNome) {
		this.skillNome = skillNome;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}