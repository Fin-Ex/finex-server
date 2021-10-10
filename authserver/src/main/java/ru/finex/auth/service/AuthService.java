package ru.finex.auth.service;

import com.google.inject.ImplementedBy;
import ru.finex.auth.service.impl.AuthServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(AuthServiceImpl.class)
public interface AuthService {
}
