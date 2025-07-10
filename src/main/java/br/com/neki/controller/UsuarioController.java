package br.com.neki.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.neki.dtos.RedefinirSenhaDTO;
import br.com.neki.dtos.SolicitarSenhaDTO;
import br.com.neki.dtos.UsuarioAtualizarDTO;
import br.com.neki.dtos.UsuarioDTO;
import br.com.neki.dtos.UsuarioInserirDTO;
import br.com.neki.exception.EmailException;
import br.com.neki.exception.RecursoNaoEncontradoException;
import br.com.neki.exception.SenhaException;
import br.com.neki.security.JwtUtil;
import br.com.neki.security.RecaptchaService;
import br.com.neki.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuário", description = "Operações relacionadas aos usuários")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	RecaptchaService recaptchaService;

	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping
	@Operation(summary = "Listar os usuários", description = "Lista todos os usuários cadastrados no sistema")
	public ResponseEntity<List<UsuarioDTO>> listar() {
		return ResponseEntity.ok(usuarioService.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar usuario por id", description = "Busca o usuario pelo id correspondente")
	public ResponseEntity<UsuarioDTO> buscar(@PathVariable Long id) {
		UsuarioDTO usuarioDTO = usuarioService.findById(id);
		if (usuarioDTO == null) {
			throw new RecursoNaoEncontradoException("Não existe usuario com o id " + id);
		}
		return ResponseEntity.ok(usuarioDTO);
	}

	@PostMapping
	@Operation(summary = "Cadastrar usuario", description = "Cadastra usuario no sistema")
	public ResponseEntity<?> inserir(@Valid @RequestBody UsuarioInserirDTO usuario) {

		boolean validCaptcha = recaptchaService.validateRecaptcha(usuario.getRecaptchaToken());
		if (!validCaptcha) {
			return ResponseEntity.badRequest().body("Falha na validação do reCAPTCHA.");
		}

		try {
			UsuarioDTO usuarioDTO = usuarioService.inserir(usuario);

			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuarioDTO.getId())
					.toUri();
			return ResponseEntity.created(uri).body(usuarioDTO);
		} catch (MessagingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao enviar e-mail. Por favor, tente novamente mais tarde.");

		} catch (EmailException | SenhaException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualiza usuário por ID", description = "Atualiza usuário pelo ID correspondente")
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id,
			@RequestBody @Valid UsuarioAtualizarDTO usuario) {
		UsuarioDTO usuarioAtualizado = usuarioService.atualizar(id, usuario);

		String perfil = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst()
				.get().getAuthority();
		String novoToken = jwtUtil.generateToken(usuarioAtualizado.getEmail(), perfil, usuarioAtualizado.getId());

		return ResponseEntity.ok().header("Authorization", "Bearer " + novoToken).body(usuarioAtualizado);
	}

	@PostMapping("/esqueci-senha")
	@Operation(summary = "Solicita redefinição de senha", description = "Solicita a redefinição da senha do usuário por e-mail")
	public ResponseEntity<?> solicitarRedefinicao(@RequestBody @Valid SolicitarSenhaDTO solicitarSenha)
			throws EmailException, MessagingException {

		boolean validCaptcha = recaptchaService.validateRecaptcha(solicitarSenha.getRecaptchaToken());
		if (!validCaptcha) {
			return ResponseEntity.badRequest().body("Falha na validação do reCAPTCHA.");
		}
		try {
			LocalDateTime expiracao = usuarioService.solicitarRedefinicaoSenha(solicitarSenha.getEmail());
			LocalDateTime servidorAgora = LocalDateTime.now();
			Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "Email enviado com sucesso!");
	        response.put("expiraEm", expiracao);
	        response.put("servidorAgora", servidorAgora);

	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/redefinir-senha")
	@Operation(summary = "Redefine a senha do usuário", description = "Redefine a senha do usuário via e-mail")
	public ResponseEntity<String> redefinirSenha(@RequestParam("token") String token,
			@RequestBody RedefinirSenhaDTO dto) {
		usuarioService.redefinirSenha(token, dto.getNovaSenha(), dto.getConfirmaSenha());
		return ResponseEntity.ok("Senha redefinida com sucesso.");
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deleta usuario por id", description = "Deleta usuario pelo id correspondente")
	public ResponseEntity<Void> remover(@PathVariable Long id) {

		Boolean validate = usuarioService.deletar(id);
		if (validate) {
			return ResponseEntity.noContent().build();
		}
		throw new RecursoNaoEncontradoException("Não existe usuario com o id " + id);
	}
}
