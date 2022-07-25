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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import ru.finex.core.model.entity.EntityObject;

import java.time.Instant;

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
    @NotNull
    private String code;

    @CreationTimestamp
    private Instant createDate;
    @UpdateTimestamp
    private Instant modifyDate;

}
