package ru.finex.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

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
@Builder
@Table(name = "user_totp_recovery_codes")
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "user_totp_recovery_codes_id_seq", sequenceName = "user_totp_recovery_codes_id_seq", allocationSize = 1)
public class TOTPRecoveryCodeEntity implements ru.finex.core.model.entity.Entity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "user_totp_recovery_codes_id_seq", strategy = GenerationType.SEQUENCE)
    private Long persistenceId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "createDate")
    private Instant createDate;
    @Column(name = "modifyDate")
    private Instant modifyDate;

}
