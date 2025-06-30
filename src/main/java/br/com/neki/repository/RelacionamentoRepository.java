package br.com.neki.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.neki.model.Relacionamento;
import br.com.neki.model.RelacionamentoPK;
import br.com.neki.model.Usuario;

public interface RelacionamentoRepository extends JpaRepository<Relacionamento, RelacionamentoPK> {

	List<Relacionamento> findByIdUsuarioId(Usuario usuario);

	Optional<List<Relacionamento>> findByIdSkillsId(Long id);

	@Query("SELECT r FROM Relacionamento r WHERE r.id.usuarioId.id = :usuarioId")
	List<Relacionamento> findRelacionamentosByUsuarioId(@Param("usuarioId") Long usuarioId);

	@Query("SELECT r FROM Relacionamento r WHERE r.id.usuarioId.id = :usuarioId AND r.id.skillsId.id = :skillsId")
	Optional<Relacionamento> findRelacionamentoByUsuarioIdAndSkillsId(@Param("usuarioId") Long usuarioId,
			@Param("skillsId") Long skillsId);

	@Modifying
	@Transactional
	@Query("DELETE FROM Relacionamento r WHERE r.id.usuarioId.id = :usuarioId AND r.id.skillsId.id = :skillsId")
	int deleteRelacionamentoByUsuarioIdAndSkillsId(@Param("usuarioId") Long usuarioId,
			@Param("skillsId") Long skillsId);
}