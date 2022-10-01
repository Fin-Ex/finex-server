package ru.finex.core.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author finfan
 */
@Data
@Entity
@Table(name = "game_object_prototypes")
@SequenceGenerator(name = "game_object_prototypes_id_seq", sequenceName = "game_object_prototypes_id_seq", allocationSize = 1)
public class GameObjectPrototype implements EntityObject<Integer> {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "game_object_prototypes_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @Column(unique = true, nullable = false)
    private String name;

    private Integer parentId;
}
