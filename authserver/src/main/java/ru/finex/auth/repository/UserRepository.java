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

    UserEntity findByLogin(String login);
    void clearSecret(Long id);

}
