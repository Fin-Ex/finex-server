package ru.finex.ws.service;

import ru.finex.ws.model.ClientSession;

import java.util.List;

/**
 * @author m0nster.mind
 */
public interface ClientService {

    /**
     * Добавить новую сетевую сессию.
     * @param client клиент
     */
    void addSession(ClientSession client);

    /**
     * Удалить сетевую сессию.
     * @param client клиент
     */
    void removeSession(ClientSession client);

    /**
     * Возвращает копию списка подключенных клиентов.
     * @return список подключенных клиентов
     */
    List<ClientSession> getSessions();

}
