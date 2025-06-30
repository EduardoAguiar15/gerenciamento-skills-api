package br.com.neki.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import br.com.neki.dtos.SkillsCompletoDTO;
import br.com.neki.dtos.RelacionamentoVincularDTO;
import br.com.neki.service.RelacionamentoService;
import br.com.neki.service.UsuarioService;
//import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/relacionamento")
public class RelacionamentoController {

	@Autowired
	RelacionamentoService relacionamentoService;

	@Autowired
	UsuarioService usuarioService;

	@PostMapping("/usuario/{idUsuario}/skills")
//	@ApiOperation(value = "Cadastrar relacionamento", notes = "Cadastra o relacionamento entre usuario e skill passando o id do usuario e da skill")
	public ResponseEntity<SkillsCompletoDTO> criarVinculo(@PathVariable Long idUsuario,
			@Valid @RequestBody RelacionamentoVincularDTO relacionamentoInserir) {

		relacionamentoInserir.setUsuarioId(idUsuario);

		SkillsCompletoDTO relacionamentoInserido = relacionamentoService.criarVinculo(relacionamentoInserir);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{skillid}")
				.buildAndExpand(relacionamentoInserir.getSkillId()).toUri();

		return ResponseEntity.created(uri).body(relacionamentoInserido);
	}

	@PutMapping("/usuario/{idUsuario}/skills")
//	@ApiOperation(value = "Atualizar relacionamento", notes = "Atualiza o relacionamento entre usuario e skill passando o id do usuario e da skill")
	public ResponseEntity<SkillsCompletoDTO> atualizarVinculo(@PathVariable Long idUsuario,
			@Valid @RequestBody RelacionamentoVincularDTO relacionamentoInserir) {
		relacionamentoInserir.setUsuarioId(idUsuario);
		SkillsCompletoDTO skillsDTO = relacionamentoService.atualizarVinculo(relacionamentoInserir);
		return ResponseEntity.ok(skillsDTO);
	}

	@GetMapping("usuario/{usuarioId}/skills")
//	@ApiOperation(value = "Buscar relacionamento por usuario", notes = "Busca todas as skills foram vinculadas a um usuario pelo id")
	public ResponseEntity<List<SkillsCompletoDTO>> listarVinculos(@PathVariable Long usuarioId) {
		List<SkillsCompletoDTO> skills = relacionamentoService.listarVinculos(usuarioId);
		return ResponseEntity.ok().body(skills);
	}

	@GetMapping("usuario/{usuarioId}/skills/{skillsId}")
//	@ApiOperation(value = "Buscar relacionamento", notes = "Busca o relacionamento entre a skill pelo id com um usuario pelo id")
	public ResponseEntity<SkillsCompletoDTO> buscarVinculo(@PathVariable Long usuarioId, @PathVariable Long skillsId) {
		SkillsCompletoDTO relacionamento = relacionamentoService.buscarVinculo(usuarioId, skillsId);
		return ResponseEntity.ok(relacionamento);
	}

	@DeleteMapping("usuario/{usuarioId}/skills/{skillsId}")
//	@ApiOperation(value = "Deletar relacionamento", notes = "Deleta o relacionamento entre uma skill pelo id com um usuario pelo id")
	public ResponseEntity<Void> remover(@PathVariable Long usuarioId, @PathVariable Long skillsId) {
		relacionamentoService.desvincularRelacionamento(usuarioId, skillsId);
		return ResponseEntity.noContent().build();
	}
}