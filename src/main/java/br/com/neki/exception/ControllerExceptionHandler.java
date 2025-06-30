package br.com.neki.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EmailException.class)
	protected ResponseEntity<Object> handleEmailException(EmailException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("success", false);
		body.put("message", ex.getMessage());
		return ResponseEntity.ok(body);
	}

	@ExceptionHandler(SenhaException.class)
	protected ResponseEntity<Object> handleSenhaException(SenhaException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("success", false);
		body.put("message", ex.getMessage());
		return ResponseEntity.ok(body);
	}

	@ExceptionHandler(NullException.class)
	protected ResponseEntity<Object> handleNullException(NullException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("success", false);
		body.put("message", ex.getMessage());
		return ResponseEntity.ok(body);
	}

	@ExceptionHandler(GlobalException.class)
	protected ResponseEntity<Object> handleGlobalException(GlobalException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("success", false);
		body.put("message", ex.getMessage());
		return ResponseEntity.ok(body);
	}

	@ExceptionHandler(TokenInvalidoException.class)
	protected ResponseEntity<Object> handleTokenInvalidoException(TokenInvalidoException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("success", false);
		body.put("message", ex.getMessage());
		return ResponseEntity.ok(body);
	}

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	protected ResponseEntity<?> handleResourceNotFoundException(RecursoNaoEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		String message = "Parâmetro inválido: '" + ex.getValue() + "'. Esperado: "
				+ ex.getRequiredType().getSimpleName();
		return ResponseEntity.badRequest().body(message);
	}

	@ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
	public ResponseEntity<Object> handleAuthenticationException(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou usuário não encontrado.");
	}
}