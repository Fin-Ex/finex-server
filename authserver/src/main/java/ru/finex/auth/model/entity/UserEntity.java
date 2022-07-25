package ru.finex.auth.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import ru.finex.core.model.entity.EntityObject;

import java.time.Instant;

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
public class UserEntity implements EntityObject<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    private Long persistenceId;

    @NotNull
    private String login;

    @NotNull
    private String password;
    @NotNull
    private String hash;

    @CreationTimestamp
    private Instant createDate;
    @UpdateTimestamp
    private Instant modifyDate;
    private Instant authDate;

    private String country;
    private String ipAddress;

    private String email;
    private String phone;
    private String secret;

}
