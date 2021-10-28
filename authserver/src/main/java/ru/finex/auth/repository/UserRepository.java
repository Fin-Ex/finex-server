package ru.finex.auth.repository;

import com.google.inject.ImplementedBy;
import ru.finex.auth.model.entity.UserEntity;
import ru.finex.auth.repository.impl.UserRepositoryImpl;
import ru.finex.core.repository.CrudRepository;

/**
 * @author m0nster.mind
 */
@ImplementedBy(UserRepositoryImpl.class)
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    /**
     * Find user by login name.
     * @param login login name
     * @return user or null if not exists
     */
    UserEntity findByLogin(String login);

    /**
     * Clear user TOTP secret.
     * @param id user ID
     */
    void clearSecret(Long id);

    /**
     * Check users secret to null.
     * @param login users login
     * @return true if users secret not null, otherwise false
     */
    boolean isSecretNotNull(String login);

}
