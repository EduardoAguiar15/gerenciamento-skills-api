package br.com.neki.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.neki.security.JwtAuthenticationFilter;
import br.com.neki.security.JwtAuthorizationFilter;
import br.com.neki.security.JwtUtil;
import br.com.neki.security.RecaptchaService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	RecaptchaService recaptchaService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
				.antMatchers(HttpMethod.POST, "/usuario", "/usuario/esqueci-senha", "/usuario/redefinir-senha")
				.permitAll().anyRequest().authenticated().and().httpBasic().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors().and().csrf().disable();
		http.addFilter(new JwtAuthenticationFilter(this.authenticationManager(), jwtUtil, recaptchaService));
		http.addFilterBefore(new JwtAuthorizationFilter(jwtUtil, userDetailsService),
				UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration
				.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://Gerenciador-skills.vercel.app"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration.applyPermitDefaultValues());
		return source;
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}