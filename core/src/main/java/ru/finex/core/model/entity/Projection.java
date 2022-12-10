package ru.finex.core.model.entity;

import jakarta.persistence.Column;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates dynamic proxy for interface to hibernate projection.
 * <p>
 * The projections is support:
 * <li>{@link Column#name() Column#name} to specify column name. If column name is not specified
 *  finex will use method name as column name (with substitution 'get' prefix and conversion to snake case).
 * <li>{@link jakarta.persistence.Enumerated Enumerated} for enum types.
 * <li>Declaring default methods.
 * <p>
 * Example:
 * <pre>{@code
 * @Projection
 * public interface AvatarPrototypeView {
 *
 *     String name(); // implicit column name: 'name'
 *
 *     @Enumerated(EnumType.STRING)
 *     Race getRace(); // enum conversion by string
 *
 *     Gender getGender(); // implicit enum conversion - ordinal
 *
 *     Integer getClassId(); // implicit column name: 'class_id'
 *
 *     @Column(name = "LUC")
 *     Integer getLuck(); // column name: 'LUC'
 * }
 * }</pre>
 * @author m0nster.mind
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Projection {
}
