package br.com.neki.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.neki.dtos.SkillsCompletoDTO;
import br.com.neki.dtos.RelacionamentoVincularDTO;
import br.com.neki.exception.GlobalException;
import br.com.neki.exception.NullException;
import br.com.neki.exception.RecursoNaoEncontradoException;
import br.com.neki.model.Relacionamento;
import br.com.neki.model.RelacionamentoPK;
import br.com.neki.model.Skills;
import br.com.neki.model.Usuario;
import br.com.neki.repository.RelacionamentoRepository;
import br.com.neki.repository.SkillsRepository;
import br.com.neki.repository.UsuarioRepository;

@Service
public class RelacionamentoService {

	@Autowired
	private RelacionamentoRepository relacionamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private SkillsRepository skillsRepository;

	public SkillsCompletoDTO criarVinculo(RelacionamentoVincularDTO relacionamentoVincularDTO) {

		if (relacionamentoVincularDTO.getUsuarioId() == null) {
			throw new NullException("O id do usuário não pode ser nulo");
		}
		if (relacionamentoVincularDTO.getSkillId() == null) {
			throw new NullException("O id da skill não pode ser nulo");
		}

		Optional<Usuario> usuarioOpt = usuarioRepository.findById(relacionamentoVincularDTO.getUsuarioId());
		Optional<Skills> skillOpt = skillsRepository.findById(relacionamentoVincularDTO.getSkillId());

		if (usuarioOpt.isEmpty() && skillOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException("Usuario e Skill não existem");
		}
		if (usuarioOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException(
					"O usuario com id " + relacionamentoVincularDTO.getUsuarioId() + " não existe");
		}
		if (skillOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException(
					"A skill com id " + relacionamentoVincularDTO.getSkillId() + " não existe");
		}

		Usuario usuario = usuarioOpt.get();
		Skills skill = skillOpt.get();

		RelacionamentoPK relacionamentoPK = new RelacionamentoPK();
		relacionamentoPK.setSkillsId(skill);
		relacionamentoPK.setUsuarioId(usuario);

		Optional<Relacionamento> relacionamentoOpt = relacionamentoRepository.findById(relacionamentoPK);
		if (relacionamentoOpt.isPresent()) {
			throw new GlobalException("O relacionamento entre o usuário ID " + usuario.getId() + " e a skill ID "
					+ skill.getId() + " já existe");
		}

		Relacionamento relacionamento = new Relacionamento();
		relacionamento.setId(relacionamentoPK);
		relacionamento.setLevel(relacionamentoVincularDTO.getLevel());

		relacionamentoRepository.save(relacionamento);

		SkillsCompletoDTO skills = new SkillsCompletoDTO();
		skills.setSkillId(skill.getId());
		skills.setSkillNome(skill.getSkillNome());
		skills.setLevel(relacionamento.getLevel());

		return skills;
	}

	public List<SkillsCompletoDTO> listarVinculos(Long usuarioId) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);

		if (usuarioOpt.isEmpty()) {
			return null;
		}
		List<Relacionamento> relacionamentos = relacionamentoRepository.findRelacionamentosByUsuarioId(usuarioId);
		if (relacionamentos.isEmpty()) {
			return new ArrayList<>();
		}
		List<SkillsCompletoDTO> relacionamentoDTOs = new ArrayList<>();

		for (Relacionamento relacionamento : relacionamentos) {
			relacionamentoDTOs.add(new SkillsCompletoDTO(relacionamento));
		}
		return relacionamentoDTOs;
	}

	public SkillsCompletoDTO buscarVinculo(Long usuarioId, Long skillsId) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
		Optional<Skills> skillOpt = skillsRepository.findById(skillsId);

		if (usuarioOpt.isEmpty() && skillOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException("Usuario e Skill não existem");
		}
		if (usuarioOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException("O usuario com id " + usuarioId + " não existe");
		}
		if (skillOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException("A skill com id " + skillsId + " não existe");
		}

		Optional<Relacionamento> relacionamentoOpt = relacionamentoRepository
				.findRelacionamentoByUsuarioIdAndSkillsId(usuarioId, skillsId);
		if (relacionamentoOpt.isEmpty()) {
			return null;
		}
		SkillsCompletoDTO relacionamentoDto = new SkillsCompletoDTO(relacionamentoOpt.get());
		return relacionamentoDto;
	}

	public SkillsCompletoDTO atualizarVinculo(RelacionamentoVincularDTO relacionamentoVincularDTO) {

		if (relacionamentoVincularDTO.getUsuarioId() == null) {
			throw new NullException("O id do usuário não pode ser nulo");
		}
		if (relacionamentoVincularDTO.getSkillId() == null) {
			throw new NullException("O id da skill não pode ser nulo");
		}

		Optional<Usuario> usuarioOpt = usuarioRepository.findById(relacionamentoVincularDTO.getUsuarioId());
		Optional<Skills> skillOpt = skillsRepository.findById(relacionamentoVincularDTO.getSkillId());

		if (usuarioOpt.isEmpty() && skillOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException("Usuario e Skill não existem");
		}
		if (usuarioOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException(
					"O usuario com id " + relacionamentoVincularDTO.getUsuarioId() + " não existe");
		}
		if (skillOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException(
					"A skill com id " + relacionamentoVincularDTO.getSkillId() + " não existe");
		}

		Usuario usuario = usuarioOpt.get();
		Skills skill = skillOpt.get();

		RelacionamentoPK relacionamentoPK = new RelacionamentoPK();
		relacionamentoPK.setSkillsId(skill);
		relacionamentoPK.setUsuarioId(usuario);

		Optional<Relacionamento> relacionamentoOpt = relacionamentoRepository.findById(relacionamentoPK);
		if (relacionamentoOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException("Skill não foi vinculada a este usuário");
		}
		Relacionamento relacionamento = new Relacionamento();
		relacionamento.setId(relacionamentoPK);
		relacionamento.setLevel(relacionamentoVincularDTO.getLevel());

		relacionamentoRepository.save(relacionamento);

		SkillsCompletoDTO skills = new SkillsCompletoDTO();
		skills.setSkillId(skill.getId());
		skills.setSkillNome(skill.getSkillNome());
		skills.setLevel(relacionamento.getLevel());

		return skills;
	}

	@Transactional
	public Boolean desvincularRelacionamento(Long usuarioId, Long skillsId) {

		if (usuarioRepository.findById(usuarioId).isEmpty() && skillsRepository.findById(skillsId).isEmpty()) {
			throw new RecursoNaoEncontradoException(
					"Usuário e skill não encontradas, insira um ID válido para usuário e skill!");
		}

		if (usuarioRepository.findById(usuarioId).isEmpty()) {
			throw new RecursoNaoEncontradoException("Usuário não encontrado, insira um ID válido para usuário!");
		}

		if (skillsRepository.findById(skillsId).isEmpty()) {
			throw new RecursoNaoEncontradoException("Skill não encontrada, insira um ID válido para Skill!");
		}

		Optional<Relacionamento> relacionamentoOpt = relacionamentoRepository
				.findRelacionamentoByUsuarioIdAndSkillsId(usuarioId, skillsId);

		if (relacionamentoOpt.isEmpty()) {
			throw new RecursoNaoEncontradoException("Relacionamento não encontrado.");
		}

		relacionamentoRepository.deleteRelacionamentoByUsuarioIdAndSkillsId(usuarioId, skillsId);
		return true;
	}
}