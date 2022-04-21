package ru.finex.core.repository;

import ru.finex.core.model.entity.Entity;
import ru.finex.core.utils.GenericUtils;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;

/**
 * Generic implementation of CRUD repository.
 *
 * @param <E> entity type
 * @param <ID> entity ID type
 * @author m0nster.mind
 */
public class DefaultCrudRepository<E extends Entity<ID>, ID extends Serializable> extends AbstractCrudRepository<E, ID> {

    /**
     * Create new builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static class Builder {

        private ExecutorService executorService;
        private Class<? extends Entity> entityType;

        public Builder executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public Builder entityType(Class<? extends Entity> entityType) {
            this.entityType = entityType;
            return this;
        }

        public Builder entityTypeFromInterface(Class<? extends CrudRepository> interfaceType) {
            entityType = GenericUtils.getInterfaceGenericType(interfaceType, CrudRepository.class, 0);
            return this;
        }

        public DefaultCrudRepository build() {
            DefaultCrudRepository repository = new DefaultCrudRepository();

            if (entityType != null) {
                repository.entityClass = entityType;
            }

            if (executorService != null) {
                repository.executorService = executorService;
            }

            return repository;
        }

    }

}
