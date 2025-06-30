package br.com.neki.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "relacionamento")
public class Relacionamento {

	@EmbeddedId()
	private RelacionamentoPK id = new RelacionamentoPK();

	@Min(0)
	@Max(10)
	@Column(nullable = false)
	private Integer level;

	public Relacionamento() {

	}

	public Relacionamento(Usuario usuario, Skills skills, Integer level) {
		this.id.setUsuarioId(usuario);
		this.id.setSkillsId(skills);
		this.level = level;
	}

	public RelacionamentoPK getId() {
		return id;
	}

	public void setId(RelacionamentoPK id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}