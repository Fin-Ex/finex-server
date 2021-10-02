package ru.finex.ws.tick.impl;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.apache.commons.lang3.reflect.MethodUtils;
import ru.finex.ws.tick.RegisterTick;
import ru.finex.ws.tick.TickService;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author m0nster.mind
 */
public class RegisterTickListener implements TypeListener {

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        Class<?> clazz = type.getRawType();

        List<Method> methods = MethodUtils.getMethodsListWithAnnotation(clazz, RegisterTick.class, true, false);
        if (methods.isEmpty()) {
            return;
        }

        Provider<TickService> tickServiceProvider = encounter.getProvider(TickService.class);
        encounter.register((InjectionListener<I>) injectee -> {
            TickService tickService = tickServiceProvider.get();

            for (Method method : methods) {
                RegisterTick registerTick = method.getAnnotation(RegisterTick.class);
                tickService.register(injectee, method, registerTick.value());
            }
        });

    }

}
