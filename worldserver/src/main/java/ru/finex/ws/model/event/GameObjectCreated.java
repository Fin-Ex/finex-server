package ru.finex.ws.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.event.GameObjectEvent;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameObjectCreated implements GameObjectEvent {

    private int runtimeId;

}
