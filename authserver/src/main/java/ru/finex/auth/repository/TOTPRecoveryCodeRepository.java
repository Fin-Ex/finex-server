package ru.finex.auth.repository;

import ru.finex.auth.model.entity.TOTPRecoveryCodeEntity;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.NamedQuery;

/**
 * @author m0nster.mind
 */
public interface TOTPRecoveryCodeRepository extends CrudRepository<TOTPRecoveryCodeEntity, Long> {

    /**
     * Delete users specified code.
     * @param userId user id
     * @param code code
     * @return true if code deleted, otherwise specified code doesnt exists
     */
    @NamedQuery
    boolean deleteByUserIdAndCode(Long userId, String code);

    /**
     * Delete all recovery codes for specified user.
     * @param userId user id
     * @return count of deleted records
     */
    @NamedQuery
    int deleteByUserId(Long userId);

}
