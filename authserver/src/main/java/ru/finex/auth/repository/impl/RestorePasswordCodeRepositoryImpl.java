package ru.finex.auth.repository.impl;

import ru.finex.auth.model.entity.RestorePasswordCodeEntity;
import ru.finex.auth.repository.RestorePasswordCodeRepository;
import ru.finex.core.repository.AbstractCrudRepository;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class RestorePasswordCodeRepositoryImpl extends AbstractCrudRepository<RestorePasswordCodeEntity, Long> implements RestorePasswordCodeRepository {
}
