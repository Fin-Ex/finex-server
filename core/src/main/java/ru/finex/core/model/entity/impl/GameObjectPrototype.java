package ru.finex.core.model.entity.impl;

import lombok.Data;
import ru.finex.core.model.entity.EntityObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author finfan
 */
@Data
@Entity
@Table(name = "game_object_prototypes")
public class GameObjectPrototype implements EntityObject<Integer> {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "game_object_prototypes_id_seq", sequenceName = "game_object_prototypes_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "game_object_prototypes_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @Column(unique = true, nullable = false)
    private String name;

    private Integer parentId;
}
