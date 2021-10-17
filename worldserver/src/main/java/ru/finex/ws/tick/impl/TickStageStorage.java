package ru.finex.ws.tick.impl;

import lombok.extern.slf4j.Slf4j;
import ru.finex.ws.tick.TickInvokeDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author m0nster.mind
 */
@Slf4j
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public class TickStageStorage {

    private final List<TickInvokeDecorator> stage = new ArrayList<>();

    public void addDecorator(TickInvokeDecorator decorator) {
        stage.add(decorator);
    }

    public void execute() {
        for (int i = 0; i < stage.size(); i++) {
            TickInvokeDecorator decorator = stage.get(i);
            try {
                decorator.onTick();
            } catch (Exception e) {
                log.error("Fail to process ticked element.", e);
            }
        }
    }

}
