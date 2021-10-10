package ru.finex.auth.repository;

import com.google.inject.ImplementedBy;
import ru.finex.auth.model.entity.RestorePasswordCodeEntity;
import ru.finex.auth.repository.impl.RestorePasswordCodeRepositoryImpl;
import ru.finex.core.repository.CrudRepository;

/**
 * @author m0nster.mind
 */
@ImplementedBy(RestorePasswordCodeRepositoryImpl.class)
public interface RestorePasswordCodeRepository extends CrudRepository<RestorePasswordCodeEntity, Long> {
}
