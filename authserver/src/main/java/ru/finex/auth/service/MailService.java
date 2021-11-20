package ru.finex.auth.service;

/**
 * @author m0nster.mind
 */
public interface MailService {

    /**
     * Send email to recipient with specified topic and content.
     * @param recipient recipient (email)
     * @param subject topic
     * @param content content
     */
    void sendMail(String recipient, String subject, String content);

}
