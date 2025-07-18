package br.com.neki.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${auth.jwt-secret}")
	private String jwtSecret;

	@Value("${auth.jwt-expiration-miliseg}")
	private Long jwtExpirationMiliseg;

	public String generateToken(String username, String perfil, Long userId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("perfil", perfil);
		claims.put("userId", userId);

		SecretKey secretKeySpec = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		return Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + this.jwtExpirationMiliseg)).signWith(secretKeySpec)
				.addClaims(claims).compact();
	}

	public boolean isValidToken(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}

	public String getUserName(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}

	public Long getUserId(String token) {
		Claims claims = getClaims(token);
		if (claims != null && claims.get("userId") != null) {
			return Long.valueOf(claims.get("userId").toString());
		}
		return null;
	}
}