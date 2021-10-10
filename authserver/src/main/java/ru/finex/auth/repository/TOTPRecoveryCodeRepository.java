package ru.finex.auth.repository;

import com.google.inject.ImplementedBy;
import ru.finex.auth.model.entity.TOTPRecoveryCodeEntity;
import ru.finex.auth.repository.impl.TOTPRecoveryCodeRepositoryImpl;
import ru.finex.core.repository.CrudRepository;

/**
 * @author m0nster.mind
 */
@ImplementedBy(TOTPRecoveryCodeRepositoryImpl.class)
public interface TOTPRecoveryCodeRepository extends CrudRepository<TOTPRecoveryCodeEntity, Long> {

    /**
     * Delete users specified code.
     * @param userId user id
     * @param code code
     * @return true if code deleted, otherwise specified code doesnt exists
     */
    boolean deleteByUserIdAndCode(Long userId, String code);

    /**
     * Delete all recovery codes for specified user.
     * @param userId user id
     * @return count of deleted records
     */
    int deleteByUserId(Long userId);

}
