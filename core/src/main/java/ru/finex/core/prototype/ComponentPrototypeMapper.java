package ru.finex.core.prototype;

import ru.finex.core.component.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Component prototype mapping interface.
 *
 * @param <P> type of component prototype
 * @param <C> type of result component
 * @author m0nster.mind
 * @see Register
 * @see Ignore
 */
public interface ComponentPrototypeMapper<P extends ComponentPrototype, C extends Component> {

    /**
     * Map component prototype to the component.
     * @param prototype component prototype
     * @return the component
     */
    C map(P prototype);

    /**
     * Disable mapper auto-registration in ComponentMappers bean.
     */
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Ignore {
    }

    /**
     * Register mapper with specified prototype & component types in ComponentMappers bean.
     * @see Register
     */
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Registers {
        Register[] value();
    }

    /**
     * Register mapper with specified prototype & component types in ComponentMappers bean.
     * <p>This annotation requires the implementation of either of the options:
     * <ul>
     *     <li>Public constructor with {@code Class<?>} argument, where class argument is
     *     component class type.</li>
     *     <li>Default public constructor</li>
     * </ul>
     * <p>If mapper is generified - all generic information isn't stored!
     * @see Ignore
     */
    @Documented
    @Repeatable(Registers.class)
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Register {

        /**
         * The prototype type.
         * @return the prototype type
         */
        Class<? extends ComponentPrototype> prototype();

        /**
         * The component type.
         * @return the component type
         */
        Class<? extends Component> component();
    }

}
