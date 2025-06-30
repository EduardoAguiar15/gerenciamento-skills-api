package br.com.neki.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7).trim();
			if (!token.isEmpty()) {
				UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
				if (authentication != null) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}

		filterChain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.isValidToken(token)) {
			String username = jwtUtil.getUserName(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		}
		return null;
	}
}