package br.com.neki.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.neki.model.Skills;

public interface SkillsRepository extends JpaRepository<Skills, Long> {
}