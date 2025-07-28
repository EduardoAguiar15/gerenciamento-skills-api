package br.com.neki.config;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailConfig {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(String para, String assunto) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(para);
		helper.setSubject(assunto);
		helper.setFrom("gerenciadorskills@gmail.com");

		String imageUrl = "https://res.cloudinary.com/dptl0qlqr/image/upload/f_auto,q_auto,w_600/bem_vindo_email_hbftei.png";

		String htmlContent = "<html><body>"
				+ "<h1 style= 'font-size: 23px;'>Bem-vindo ao sistema de gerenciamento de skills!</h1><br>"
				+ "<img src='" + imageUrl + "' alt='Bem-vindo' style='max-width:100%; height:auto;'/>"
				+ "</body></html>";

		helper.setText(htmlContent, true);

		javaMailSender.send(message);
	}

	public void sendPasswordResetEmail(String para, String link) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(para);
		helper.setSubject("Redefinição de senha");
		helper.setFrom("gerenciadorskills@gmail.com");

		String htmlContent = "<html><body>" + "<p>Recebemos uma solicitação para redefinir sua senha.</p>"
				+ "<p><a href='" + link + "'>Clique aqui para redefinir sua senha</a></p>"
				+ "<p>Este link é válido por 10 minutos.</p>" + "<hr>"
				+ "<p style='font-size: 0.9em; color: #555;'>Se você não solicitou essa redefinição, pode ignorar este e-mail com segurança.</p>"
				+ "</body></html>";

		helper.setText(htmlContent, true);
		javaMailSender.send(message);
	}
}
