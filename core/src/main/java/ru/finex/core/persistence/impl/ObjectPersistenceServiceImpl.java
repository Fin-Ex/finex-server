package ru.finex.core.persistence.impl;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.finex.core.GlobalContext;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.core.persistence.ObjectPersistenceService;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.core.persistence.PersistenceObject;
import ru.finex.core.persistence.PersistenceService;

import java.lang.reflect.Field;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ObjectPersistenceServiceImpl implements ObjectPersistenceService {

    @Transactional(TxType.REQUIRES_NEW)
    @Override
    public void persist(PersistenceObject object) {
        FieldUtils.getFieldsListWithAnnotation(object.getClass(), PersistenceField.class)
            .forEach(e -> persist(object, e, e.getAnnotation(PersistenceField.class).value()));
    }

    protected void persist(PersistenceObject object, Field field, Class<? extends PersistenceService> persistenceServiceType) {
        PersistenceService persistenceService = GlobalContext.injector.getInstance(persistenceServiceType);
        try {
            EntityObject entity = (EntityObject) FieldUtils.readField(field, object, true);
            if (entity != null) {
                persist(entity, persistenceService);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    protected void persist(EntityObject entity, PersistenceService persistenceService) {
        persistenceService.persist(entity);
    }

    @Override
    public void restore(PersistenceObject object) {
        FieldUtils.getFieldsListWithAnnotation(object.getClass(), PersistenceField.class)
            .forEach(e -> restore(object, e, e.getAnnotation(PersistenceField.class).value()));
    }

    protected void restore(PersistenceObject object, Field field, Class<? extends PersistenceService> persistenceServiceType) {
        PersistenceService persistenceService = GlobalContext.injector.getInstance(persistenceServiceType);
        try {
            EntityObject reference = (EntityObject) FieldUtils.readField(field, object, true);
            EntityObject entity = persistenceService.restore(object.getPersistenceId(), reference);
            if (entity != null) {
                FieldUtils.writeField(field, object, entity, true);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
