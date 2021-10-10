package ru.finex.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "users")
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
public class UserEntity implements ru.finex.core.model.entity.Entity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    private Long persistenceId;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "create_date")
    private Instant createDate;
    @Column(name = "modify_date")
    private Instant modifyDate;
    @Column(name = "auth_date")
    private Instant authDate;

    @Column(name = "country")
    private String country;
    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "secret")
    private String secret;

}
