package br.com.neki.security;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Service
public class RecaptchaService {
	@Value("${google.recaptcha.verify.url}")
	private String recaptchaVerifyUrl;

	@Value("${recaptcha.secret.key}")
	private String recaptchaSecretKey;

	public boolean validateRecaptcha(String recaptchaToken) {
		if (recaptchaToken == null || recaptchaToken.isEmpty()) {
			System.err.println("Erro: Token reCAPTCHA está vazio ou inválido!");
			return false;
		}

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("secret", recaptchaSecretKey);
		params.add("response", recaptchaToken);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(recaptchaVerifyUrl,
					org.springframework.http.HttpMethod.POST, request,
					new ParameterizedTypeReference<Map<String, Object>>() {
					});

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				Boolean success = Boolean.TRUE.equals(response.getBody().get("success"));
				if (!success) {
					System.err.println("Erro na validação do reCAPTCHA! Resposta: " + response.getBody());
				}
				return success;
			}
		} catch (Exception e) {
			System.err.println("Erro ao validar reCAPTCHA: " + e.getMessage());
		}

		return false;
	}
}