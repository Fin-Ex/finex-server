package ru.finex.ws.service.impl;

import lombok.RequiredArgsConstructor;
import ru.finex.ws.model.entity.GameObjectComponentPrototype;
import ru.finex.ws.repository.GameObjectComponentPrototypeRepository;
import ru.finex.ws.service.GameObjectPrototypeService;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectPrototypeServiceImpl implements GameObjectPrototypeService {

    private final GameObjectComponentPrototypeRepository componentRepository;

    @Override
    public List<String> getComponentsByPrototypeName(String objectName) {
        return componentRepository.findByGameObjectTemplateName(objectName)
            .stream()
            .map(GameObjectComponentPrototype::getComponent)
            .collect(Collectors.toList());
    }
}
