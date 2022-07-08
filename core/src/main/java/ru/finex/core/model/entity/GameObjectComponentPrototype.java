package ru.finex.core.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author finfan
 */
@Data
@Entity
@Table(name = "game_object_component_prototypes")
@NoArgsConstructor
public class GameObjectComponentPrototype implements EntityObject<Integer> {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "game_object_component_prototypes_id_seq", sequenceName = "game_object_component_prototypes_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "game_object_component_prototypes_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @Column(nullable = false)
    private String component;

    @JoinColumn(name = "prototype_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GameObjectPrototype gameObjectPrototype;

    @Column(nullable = false)
    @Type(type = "RawJsonb")
    private String data;

    private Integer parentId;

    public GameObjectComponentPrototype(String component, String data) {
        this.component = component;
        this.data = data;
    }
}
