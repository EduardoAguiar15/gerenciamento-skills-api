package br.com.neki.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.neki.dtos.LoginDTO;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JwtUtil jwtUtil;

	private RecaptchaService recaptchaService;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
			RecaptchaService recaptchaService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.recaptchaService = recaptchaService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			LoginDTO login = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);

			boolean isValidRecaptcha = recaptchaService.validateRecaptcha(login.getRecaptchaToken());

			if (!isValidRecaptcha) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Erro: reCAPTCHA inválido!");
				response.getWriter().flush();
				return null;
			}

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					login.getUsername().toLowerCase(), login.getPassword(), new ArrayList<>());

			Authentication auth = authenticationManager.authenticate(authToken);
			return auth;

		} catch (UsernameNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.getWriter().write("Usuário não encontrado.");
				response.getWriter().flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			return null;

		} catch (BadCredentialsException e) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			try {
				response.getWriter().write("{\"success\": false, \"message\": \"Credenciais inválidas.\"}");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				response.getWriter().flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			try {
				response.getWriter().write("Erro inesperado.");
				response.getWriter().flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			return null;
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		Long userId = ((UsuarioDetalhe) authResult.getPrincipal()).getId();
		String username = ((UserDetails) authResult.getPrincipal()).getUsername();
		String perfil = ((UserDetails) authResult.getPrincipal()).getAuthorities().stream().findFirst().get()
				.getAuthority();
		String token = jwtUtil.generateToken(username, perfil, userId);
		response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
	}
}