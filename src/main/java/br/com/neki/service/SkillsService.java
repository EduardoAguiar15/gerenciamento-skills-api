package br.com.neki.service;

import br.com.neki.dtos.SkillsDTO;
import br.com.neki.exception.RecursoNaoEncontradoException;
import br.com.neki.model.Skills;
import br.com.neki.repository.SkillsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillsService {

	@Autowired
	private SkillsRepository skillsRepository;

	public List<SkillsDTO> findAll() {
		List<Skills> skills = skillsRepository.findAll();
		List<SkillsDTO> skillsDTO = skills.stream().map(skill -> new SkillsDTO(skill)).collect(Collectors.toList());
		return skillsDTO;
	};

	public SkillsDTO findById(Long id) {
		Optional<Skills> skills = skillsRepository.findById(id);
		if (skills.isEmpty()) {
			throw new RecursoNaoEncontradoException("Skill não encontrada com o id: " + id);
		}
		SkillsDTO skillsDTO = new SkillsDTO(skills.get());
		return skillsDTO;
	};
}