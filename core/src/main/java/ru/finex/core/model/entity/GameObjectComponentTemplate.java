package ru.finex.core.model.entity;

import lombok.Data;

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
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
@Data
@Entity
@Table(name = "game_object_component_templates")
public class GameObjectComponentTemplate implements ru.finex.core.model.entity.Entity {
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "game_object_component_templates_id_seq", sequenceName = "game_object_component_templates_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "game_object_component_templates_id_seq", strategy = GenerationType.SEQUENCE)
	private int persistenceId;
	@Column(name = "component", nullable = false)
	private String component;
	@JoinColumn(name = "game_object_template_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private GameObjectTemplate gameObjectTemplate;
}
