package br.com.neki.model;

import javax.persistence.*;

@Entity
public class Skills {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_skill", unique = true)
	private Long id;

	@Column(name = "skill_nome")
	private String skillNome;

	public Skills() {
	}

	public Skills(Long id, String skillNome) {
		this.id = id;
		this.skillNome = skillNome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSkillNome() {
		return skillNome;
	}

	public void setSkillNome(String skillNome) {
		this.skillNome = skillNome;
	}
}