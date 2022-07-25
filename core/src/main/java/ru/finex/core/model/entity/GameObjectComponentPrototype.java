package ru.finex.core.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.finex.core.db.impl.RawJsonUserType;

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
    @Type(RawJsonUserType.class)
    private String data;

    private Integer parentId;

    public GameObjectComponentPrototype(String component, String data) {
        this.component = component;
        this.data = data;
    }
}
