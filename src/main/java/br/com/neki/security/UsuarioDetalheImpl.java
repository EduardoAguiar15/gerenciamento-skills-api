package br.com.neki.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.neki.model.Usuario;
import br.com.neki.repository.UsuarioRepository;

@Service
public class UsuarioDetalheImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username.toLowerCase());
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado: " + username);
		}
		return new UsuarioDetalhe(usuario);
	}
}