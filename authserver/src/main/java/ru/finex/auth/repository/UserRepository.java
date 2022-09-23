package ru.finex.auth.repository;

import ru.finex.auth.model.entity.UserEntity;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.NamedQuery;

/**
 * @author m0nster.mind
 */
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    /**
     * Find user by login name.
     * @param login login name
     * @return user or null if not exists
     */
    @NamedQuery
    UserEntity findByLogin(String login);

    /**
     * Clear user TOTP secret.
     * @param userId user ID
     */
    @NamedQuery
    void clearSecret(Long userId);

    /**
     * Check users secret to null.
     * @param login users login
     * @return true if users secret not null, otherwise false
     */
    @NamedQuery
    boolean isSecretNotNull(String login);

    /**
     * Update authDate for specified user to current date.
     * @param userId user ID
     */
    @NamedQuery
    void updateAuthDate(Long userId);

}
