package ru.finex.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import ru.finex.core.model.entity.EntityObject;

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
@Table(name = "user_restore_password_codes")
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "user_restore_password_codes_id_seq", sequenceName = "user_restore_password_codes_id_seq", allocationSize = 1)
public class RestorePasswordCodeEntity implements EntityObject<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "user_restore_password_codes_id_seq", strategy = GenerationType.SEQUENCE)
    private Long persistenceId;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "createDate")
    private Instant createDate;
    @Column(name = "modifyDate")
    private Instant modifyDate;

}
