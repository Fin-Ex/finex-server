package ru.finex.core.prototype;

import ru.finex.core.component.Component;

/**
 * Component prototype mapping interface.
 *
 * @param <P> type of component prototype
 * @param <C> type of result component
 * @author m0nster.mind
 */
public interface ComponentPrototypeMapper<P extends ComponentPrototype, C extends Component> {

    /**
     * Map component prototype to the component.
     * @param prototype component prototype
     * @return the component
     */
    C map(P prototype);

}
