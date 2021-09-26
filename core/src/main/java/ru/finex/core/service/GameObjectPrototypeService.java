package ru.finex.core.service;

import java.util.List;

/**
 * @author m0nster.mind
 */
public interface GameObjectPrototypeService {

    List<String> getComponentsByPrototypeName(String objectName);

}
