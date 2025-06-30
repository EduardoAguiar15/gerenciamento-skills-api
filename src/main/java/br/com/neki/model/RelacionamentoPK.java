package br.com.neki.model;

import java.io.Serializable;

import java.util.Objects;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class RelacionamentoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuarioId;

	@ManyToOne
	@JoinColumn(name = "id_skills")
	private Skills skillsId;

	public Usuario getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Usuario usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Skills getSkillsId() {
		return skillsId;
	}

	public void setSkillsId(Skills skillsId) {
		this.skillsId = skillsId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(skillsId, usuarioId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelacionamentoPK other = (RelacionamentoPK) obj;
		return Objects.equals(skillsId, other.skillsId) && Objects.equals(usuarioId, other.usuarioId);
	}
}