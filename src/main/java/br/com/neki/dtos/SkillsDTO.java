package br.com.neki.dtos;

import br.com.neki.model.Skills;

public class SkillsDTO {

	private Long skillId;

	private String skillNome;

	public SkillsDTO() {
	}

	public SkillsDTO(Skills skills) {
		this.skillId = skills.getId();
		this.skillNome = skills.getSkillNome();
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
}