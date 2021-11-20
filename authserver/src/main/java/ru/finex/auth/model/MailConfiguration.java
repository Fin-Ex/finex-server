package ru.finex.auth.model;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

import java.util.Properties;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class MailConfiguration {

    private String host;
    private int port;
    private String user;
    private String password;
    private String from;

    /**
     * Convert configuration to javax.mail properties ready.
     * @return properties
     */
    public Properties toProperties() {
        Properties properties = new Properties();
        properties.put("mail.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.user", user);
        properties.put("mail.from", from);
        return properties;
    }

}
