package ru.finex.auth.service.impl;

import ru.finex.auth.model.MailConfiguration;
import ru.finex.auth.service.MailService;

import java.util.Date;
import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * @author m0nster.mind
 */
public class MailServiceImpl implements MailService {

    private final MailConfiguration configuration;
    private final Session session;

    @Inject
    public MailServiceImpl(MailConfiguration configuration) {
        this.configuration = configuration;
        this.session = Session.getInstance(configuration.toProperties());
    }

    @Override
    public void sendMail(String recipient, String subject, String content) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(RecipientType.TO, recipient);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(content);
            Transport.send(message, configuration.getUser(), configuration.getPassword());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
