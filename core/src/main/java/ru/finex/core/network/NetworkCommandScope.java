package ru.finex.core.network;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;

import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public class NetworkCommandScope implements Scope {

    private static final ThreadLocal<NetworkCommandContext> CTX = new ThreadLocal<>();

    /**
     * Save network command context to create a command or run command.
     * @param context context
     */
    public void saveContext(NetworkCommandContext context) {
        CTX.set(context);
    }

    /**
     * Remove network command context.
     * Invoke it only if command already created or command executed.
     */
    public void removeContext() {
        CTX.remove();
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return () -> {
            Object result = null;
            NetworkCommandContext context = CTX.get();
            Type requiredType = key.getTypeLiteral().getType();
            if (requiredType instanceof Class clazz) {
                if (PacketSerializer.class.isAssignableFrom(clazz)) {
                    result = context.getSerializer();
                } else if (PacketDeserializer.class.isAssignableFrom(clazz)) {
                    result = context.getDeserializer();
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
                    if (result instanceof PacketSerializer serializer) {
                        context.setSerializer(serializer);
                    } else if (result instanceof PacketDeserializer deserializer) {
                        context.setDeserializer(deserializer);
                    } else if (result instanceof PacketMetadata metadata) {
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