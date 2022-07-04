package ru.finex.core.prototype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ru.finex.core.model.entity.impl.GameObjectComponentPrototype;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.core.prototype.GameObjectPrototypeService;
import ru.finex.core.repository.impl.GameObjectComponentPrototypeRepository;
import ru.finex.core.utils.ClassUtils;

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
    private final ObjectMapper objectMapper;

    @Override
    public List<ComponentPrototype> getPrototypesByName(String prototypeName) {
        return componentRepository.findPrototypesByPrototypeName(prototypeName)
            .stream()
            .map(this::mapPrototype)
            .collect(Collectors.toList());
    }

    private ComponentPrototype mapPrototype(GameObjectComponentPrototype prototype) {
        Class<? extends ComponentPrototype> type = ClassUtils.forName(prototype.getComponent())
            .asSubclass(ComponentPrototype.class);

        try {
            return objectMapper.readValue(prototype.getData(), type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
