package br.com.neki.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.neki.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Usuario findByEmail(String Email);

	Usuario findByTokenRedefinicaoSenha(String token);
}