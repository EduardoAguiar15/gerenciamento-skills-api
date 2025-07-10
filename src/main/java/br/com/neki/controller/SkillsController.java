package br.com.neki.controller;

import br.com.neki.dtos.SkillsDTO;
import br.com.neki.exception.RecursoNaoEncontradoException;
import br.com.neki.service.SkillsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/skills")
@Tag(name = "Skills", description = "Operações relacionadas as skills")
public class SkillsController {

	@Autowired
	private SkillsService skillsService;

	@GetMapping
	@Operation(summary = "Listar todas as skills", description = "Listagem de todas as skills cadastradas no sistema")
	public ResponseEntity<List<SkillsDTO>> listar() {
		return ResponseEntity.ok(skillsService.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar as skills por id", description = "Busca a skill pelo id correspondente")
	public ResponseEntity<SkillsDTO> buscar(@PathVariable Long id) {
		SkillsDTO skillsDTO = skillsService.findById(id);
		if (skillsDTO == null) {
			throw new RecursoNaoEncontradoException("Não existe skill com o id " + id);
		}
		return ResponseEntity.ok(skillsDTO);
	}
}
