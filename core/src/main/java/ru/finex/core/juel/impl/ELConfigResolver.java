package ru.finex.core.juel.impl;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;

/**
 * @author m0nster.mind
 */
public class ELConfigResolver extends ELResolver {

    /**
     * Create read-only config resolver.
     */
    public ELConfigResolver() {
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return isResolvable(base) ? Object.class : null;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (!isResolvable(base)) {
            return null;
        }

        Config config = (Config) base;
        Iterator<Entry<String, ConfigValue>> iterator = config.entrySet().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public FeatureDescriptor next() {
                Entry<String, ConfigValue> entry = iterator.next();
                String key = entry.getKey();

                FeatureDescriptor feature = new FeatureDescriptor();
                feature.setDisplayName(key == null ? "null" : key);
                feature.setName(feature.getDisplayName());
                feature.setShortDescription("");
                feature.setExpert(true);
                feature.setHidden(false);
                feature.setPreferred(true);
                feature.setValue(TYPE, String.class);
                feature.setValue(RESOLVABLE_AT_DESIGN_TIME, true);
                return feature;
            }
        };
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }

        Class<?> result = null;
        if (isResolvable(base)) {
            result = Object.class;
            context.setPropertyResolved(true);
        }

        return result;
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }

        Object result = null;
        if (isResolvable(base)) {
            Config config = (Config) base;
            String path = property.toString();
            if (config.hasPath(path)) {
                result = config.getValue(path).unwrapped();
            }

            context.setPropertyResolved(true);
        }

        return result;
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }

        if (isResolvable(base)) {
            context.setPropertyResolved(true);
        }

        return true;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        throw new PropertyNotWritableException("resolver is read-only");
    }

    private boolean isResolvable(Object base) {
        return base instanceof Config;
    }
}
