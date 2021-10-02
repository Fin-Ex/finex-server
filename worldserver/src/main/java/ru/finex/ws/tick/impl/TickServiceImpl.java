package ru.finex.ws.tick.impl;

import lombok.Getter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation.Simple;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import ru.finex.ws.command.InputCommandService;
import ru.finex.ws.command.PhysicsCommandService;
import ru.finex.ws.command.UpdateCommandService;
import ru.finex.ws.tick.TickInvokeDecorator;
import ru.finex.ws.tick.TickService;
import ru.finex.ws.tick.TickStage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class TickServiceImpl implements TickService {

    private final TickStageStorage[] tickStorages;

    private final InputCommandService inputCommandService;
    private final PhysicsCommandService physicsCommandService;
    private final UpdateCommandService updateCommandService;

    @Getter
    private float deltaTime;
    @Getter
    private long deltaTimeMillis;

    @Inject
    public TickServiceImpl(InputCommandService inputCommandService, PhysicsCommandService physicsCommandService,
        UpdateCommandService updateCommandService) {
        this.inputCommandService = inputCommandService;
        this.physicsCommandService = physicsCommandService;
        this.updateCommandService = updateCommandService;
        tickStorages = new TickStageStorage[TickStage.count()];

        for (int i = 0; i < tickStorages.length; i++) {
            tickStorages[i] = new TickStageStorage();
        }
    }

    @Override
    public void register(Object instance, Method method, TickStage stage) {
        Class<? extends TickInvokeDecorator> decoratorType = defineDecorator(instance, method);

        TickInvokeDecorator decorator;
        try {
            Constructor<? extends TickInvokeDecorator> constructor = decoratorType.getConstructor(instance.getClass());
            decorator = constructor.newInstance(instance);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        tickStorages[stage.ordinal()].addDecorator(decorator);
    }

    private Class<? extends TickInvokeDecorator> defineDecorator(Object instance, Method method) {
        Constructor defaultCtor;
        try {
            defaultCtor = Object.class.getConstructor();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        Unloaded<Object> definedClass = new ByteBuddy()
            .subclass(Object.class)
            .implement(TickInvokeDecorator.class)
            .name(instance.getClass().getCanonicalName() + "$Tickable$" + method.getName())
            .defineField("object", instance.getClass(), Visibility.PRIVATE)
            .defineConstructor(Visibility.PUBLIC)
            .withParameter(instance.getClass(), "object")
            .intercept(MethodCall.invoke(defaultCtor).onSuper()
                .andThen(FieldAccessor.ofField("object").setsArgumentAt(0))
            )
            .defineMethod("onTick", void.class, Visibility.PUBLIC)
            .intercept(MethodCall.invoke(method).onField("object")
                .andThen(new Simple(MethodReturn.VOID))
            ).make();

        return (Class<? extends TickInvokeDecorator>) definedClass.load(getClass().getClassLoader())
            .getLoaded();
    }

    @Override
    public void tick() {
        long startTime = System.currentTimeMillis();

        inputTick();
        physicsTick();
        updateTick();

        deltaTimeMillis = System.currentTimeMillis() - startTime;
        deltaTime = deltaTimeMillis / 1000f;
    }

    private void inputTick() {
        inputCommandService.executeCommands();
        tickStorage(TickStage.INPUT_STAGES);
    }

    private void physicsTick() {
        physicsCommandService.executeCommands();
        tickStorage(TickStage.PHYSICS_STAGES);
    }

    private void updateTick() {
        updateCommandService.executeCommands();
        tickStorage(TickStage.UPDATE_STAGES);
    }

    private void tickStorage(TickStage[] stages) {
        for (int i = 0; i < stages.length; i++) {
            TickStage stage = stages[i];
            TickStageStorage tickStorage = tickStorages[stage.ordinal()];
            tickStorage.execute();
        }
    }

}
