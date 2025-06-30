package br.com.neki.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.neki.config.MailConfig;
import br.com.neki.dtos.UsuarioAtualizarDTO;
import br.com.neki.dtos.UsuarioDTO;
import br.com.neki.dtos.UsuarioInserirDTO;
import br.com.neki.exception.EmailException;
import br.com.neki.exception.RecursoNaoEncontradoException;
import br.com.neki.exception.SenhaException;
import br.com.neki.exception.TokenInvalidoException;
import br.com.neki.model.Relacionamento;
import br.com.neki.model.Usuario;
import br.com.neki.repository.RelacionamentoRepository;
import br.com.neki.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RelacionamentoRepository relacionamentoRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MailConfig mailConfig;

	@Value("${app.reset-password.base-url}")
	private String baseUrl;

	public List<UsuarioDTO> findAll() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		List<UsuarioDTO> usuariosDTO = usuarios.stream().map(usuario -> new UsuarioDTO(usuario))
				.collect(Collectors.toList());
		return usuariosDTO;
	};

	public UsuarioDTO findById(Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isEmpty()) {
			throw new RecursoNaoEncontradoException("Usuário não encontrado com o id: " + id);
		}
		UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.get());
		return usuarioDTO;
	};

	public UsuarioDTO inserir(UsuarioInserirDTO usuarioInserirDTO)
			throws EmailException, AddressException, MessagingException {
		if (!usuarioInserirDTO.getSenha().equalsIgnoreCase(usuarioInserirDTO.getConfirmaSenha())) {
			throw new SenhaException("Senha e Confirma Senha devem ser iguais");
		}
		Usuario usuarioEmailExistente = usuarioRepository.findByEmail(usuarioInserirDTO.getEmail());
		if (usuarioEmailExistente != null) {
			throw new EmailException("Email já cadastrado.");
		}

		Usuario usuario = new Usuario();
		usuario.setEmail(usuarioInserirDTO.getEmail());
		usuario.setSenha(bCryptPasswordEncoder.encode(usuarioInserirDTO.getSenha()));
		usuario.setDataCadastro(LocalDateTime.now());

		usuario = usuarioRepository.save(usuario);

		mailConfig.sendEmail(usuario.getEmail(), "Cadastro concluido com sucesso!");

		UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
		return usuarioDTO;
	};

	public UsuarioDTO atualizar(Long id, UsuarioAtualizarDTO usuarioAtualizarDTO) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
		if (usuarioOptional.isEmpty()) {
			throw new RecursoNaoEncontradoException("Usuário não encontrado com o id: " + id);
		}

		Usuario usuario = usuarioOptional.get();

		if ((usuarioAtualizarDTO.getSenhaAtual() != null && usuarioAtualizarDTO.getNovaSenha() == null)
				|| (usuarioAtualizarDTO.getSenhaAtual() == null && usuarioAtualizarDTO.getNovaSenha() != null)) {
			throw new SenhaException("Para alterar a senha, você deve informar a senha atual e a nova senha.");
		}

		if (usuarioAtualizarDTO.getSenhaAtual() != null && usuarioAtualizarDTO.getNovaSenha() != null
				&& !usuarioAtualizarDTO.getSenhaAtual().isBlank() && !usuarioAtualizarDTO.getNovaSenha().isBlank()) {

			if (!bCryptPasswordEncoder.matches(usuarioAtualizarDTO.getSenhaAtual(), usuario.getSenha())) {
				throw new SenhaException("Senha atual incorreta.");
			}
			if (usuarioAtualizarDTO.getSenhaAtual().equals(usuarioAtualizarDTO.getNovaSenha())) {
				throw new SenhaException("A nova senha deve ser diferente da senha atual.");
			}

			usuario.setSenha(bCryptPasswordEncoder.encode(usuarioAtualizarDTO.getNovaSenha()));
		}
		Usuario usuarioEmailExistente = usuarioRepository.findByEmail(usuarioAtualizarDTO.getEmail());

		if (usuarioEmailExistente != null && !usuarioEmailExistente.getId().equals(id)) {
			throw new EmailException("Email já cadastrado.");
		}

		if (isRequisicaoSemAlteracao(usuarioAtualizarDTO, usuario)) {
			throw new EmailException("O Novo email deve ser diferente.");
		}

		usuario.setId(id);
		usuario.setEmail(usuarioAtualizarDTO.getEmail());

		Usuario usuarioAtualizado = usuarioRepository.save(usuario);

		UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioAtualizado);

		return usuarioDTO;
	};

	private boolean isRequisicaoSemAlteracao(UsuarioAtualizarDTO dto, Usuario usuario) {
		boolean senhaVazia = (dto.getSenhaAtual() == null || dto.getSenhaAtual().isBlank())
				&& (dto.getNovaSenha() == null || dto.getNovaSenha().isBlank());
		boolean emailIgual = usuario.getEmail().equals(dto.getEmail());
		return senhaVazia && emailIgual;
	}

	public LocalDateTime solicitarRedefinicaoSenha(String email) throws EmailException, MessagingException {
		Usuario usuario = usuarioRepository.findByEmail(email);

		if (usuario == null) {
			return LocalDateTime.now().plusMinutes(10);
		}

		String token = UUID.randomUUID().toString();
		LocalDateTime expiracao = LocalDateTime.now().plusMinutes(10);

		String link = baseUrl + "?token=" + token;

		usuario.setTokenRedefinicaoSenha(token);
		usuario.setDataExpiracaoToken(expiracao);
		usuarioRepository.save(usuario);

		mailConfig.sendPasswordResetEmail(usuario.getEmail(), link);

		return expiracao;
	}

	public void redefinirSenha(String token, String novaSenha, String confirmaSenha) {
		Usuario usuario = usuarioRepository.findByTokenRedefinicaoSenha(token);

		if (usuario == null || usuario.getDataExpiracaoToken() == null
				|| usuario.getDataExpiracaoToken().isBefore(LocalDateTime.now())) {
			throw new TokenInvalidoException("Token inválido ou expirado.");
		}

		if (!novaSenha.equals(confirmaSenha)) {
			throw new SenhaException("Nova Senha e Confirma Senha devem ser iguais.");
		}

		usuario.setSenha(bCryptPasswordEncoder.encode(novaSenha));
		usuario.setTokenRedefinicaoSenha(null);
		usuario.setDataExpiracaoToken(null);

		usuarioRepository.save(usuario);
	}

	@Transactional
	public Boolean deletar(Long id) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

		if (usuarioOpt.isPresent()) {
			Usuario usuario = usuarioOpt.get();

			List<Relacionamento> relacionamentos = relacionamentoRepository.findByIdUsuarioId(usuario);

			for (Relacionamento r : relacionamentos) {
				relacionamentoRepository.delete(r);
			}

			usuarioRepository.deleteById(id);
			return true;
		}

		return false;
	}
}