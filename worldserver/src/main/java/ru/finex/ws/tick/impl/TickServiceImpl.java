package ru.finex.ws.tick.impl;

import lombok.Getter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation.Simple;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import ru.finex.core.command.NetworkCommandQueue;
import ru.finex.ws.command.PhysicsCommandQueue;
import ru.finex.ws.command.UpdateCommandQueue;
import ru.finex.ws.tick.TickInvokeDecorator;
import ru.finex.ws.tick.TickService;
import ru.finex.ws.tick.TickStage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class TickServiceImpl implements TickService {

    private static final long TICKS_PER_SEC = 20;
    private final Thread thread = new Thread(this::tickThread, "TickThread");

    private final TickStageStorage[] tickStorages;

    private final NetworkCommandQueue inputCommandService;
    private final PhysicsCommandQueue physicsCommandService;
    private final UpdateCommandQueue updateCommandService;

    @Getter
    private float deltaTime;
    @Getter
    private long deltaTimeMillis;

    @Inject
    public TickServiceImpl(NetworkCommandQueue inputCommandService, PhysicsCommandQueue physicsCommandService,
        UpdateCommandQueue updateCommandService) {
        this.inputCommandService = inputCommandService;
        this.physicsCommandService = physicsCommandService;
        this.updateCommandService = updateCommandService;
        tickStorages = new TickStageStorage[TickStage.count()];

        for (int i = 0; i < tickStorages.length; i++) {
            tickStorages[i] = new TickStageStorage();
        }
    }

    @PostConstruct
    private void start() {
        thread.start();
    }

    @PreDestroy
    private void destroy() {
        thread.stop();
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

    private void tickThread() {
        for (; ; ) {
            tick();

            long maxTickTime = 1000L / TICKS_PER_SEC;
            try {
                Thread.sleep(Math.max(maxTickTime - deltaTimeMillis, 0));
            } catch (InterruptedException e) {
                break;
            }
        }
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
        executeTickStages(TickStage.INPUT_STAGES);
    }

    private void physicsTick() {
        physicsCommandService.executeCommands();
        executeTickStages(TickStage.PHYSICS_STAGES);
    }

    private void updateTick() {
        updateCommandService.executeCommands();
        executeTickStages(TickStage.UPDATE_STAGES);
    }

    private void executeTickStages(TickStage[] stages) {
        for (int i = 0; i < stages.length; i++) {
            TickStage stage = stages[i];
            TickStageStorage tickStorage = tickStorages[stage.ordinal()];
            tickStorage.execute();
        }
    }

}
