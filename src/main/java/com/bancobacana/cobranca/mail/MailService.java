package com.bancobacana.cobranca.mail;

import com.bancobacana.cobranca.model.DebtEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(DebtEmail debtEmail) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(debtEmail.getEmailAddress());
        message.setSubject("Sua Fatura Mensal - Banco Bacana");
        message.setText("Olá, " + debtEmail.getCostumerName().toUpperCase() + "!\n" +
                "Sua fatura desse mês está fechada em R$ " + debtEmail.getOwnedAmount() + ", e vence em: " + debtEmail.getExpirationDate() + ".\n" +
                " Pague antes da data de vencimento para aproveitar nossos beneficios bacanas!");

        emailSender.send(message);
    }
}