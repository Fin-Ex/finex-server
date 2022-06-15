package ru.finex.core.command.network;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import ru.finex.core.command.CommandScope;
import ru.finex.core.network.PacketMetadata;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.network.netty.model.NetworkDto;

import java.lang.reflect.Type;

/**
 * Скоуп сетевой команды.
 *
 * @author m0nster.mind
 */
public class NetworkCommandScope implements Scope, CommandScope<NetworkCommandContext> {

    private static final ThreadLocal<NetworkCommandContext> CTX = new ThreadLocal<>();

    @Override
    public void enterScope(NetworkCommandContext context) {
        CTX.set(context);
    }

    @Override
    public void exitScope() {
        CTX.remove();
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return () -> {
            NetworkCommandContext context = CTX.get();
            if (context == null) {
                return unscoped.get();
            }

            Object result = null;
            Type requiredType = key.getTypeLiteral().getType();
            if (requiredType instanceof Class clazz) {
                if (PacketMetadata.class.isAssignableFrom(clazz)) {
                    result = context.getMetadata();
                } else if (NetworkDto.class.isAssignableFrom(clazz)) {
                    result = context.getDto();
                } else if (ClientSession.class.isAssignableFrom(clazz)) {
                    result = context.getSession();
                } else {
                    result = context.getVariables().get(clazz);
                }
            }

            if (result == null) {
                result = unscoped.get();
                if (!Scopes.isCircularProxy(requiredType)) {
                    if (result instanceof PacketMetadata metadata) {
                        context.setMetadata(metadata);
                    } else if (result instanceof NetworkDto dto) {
                        context.setDto(dto);
                    } else if (result instanceof ClientSession session) {
                        context.setSession(session);
                    } else if (requiredType instanceof Class clazz) {
                        context.getVariables().put(clazz, result);
                    }
                }
            }

            return (T) result;
        };
    }
}
