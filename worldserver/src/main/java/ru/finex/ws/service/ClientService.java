package ru.finex.ws.service;

import ru.finex.ws.model.Client;

import java.util.List;

/**
 * @author m0nster.mind
 */
public interface ClientService {

    /**
     * Добавить новую сетевую сессию.
     * @param client клиент
     */
    void addSession(Client client);

    /**
     * Удалить сетевую сессию.
     * @param client клиент
     */
    void removeSession(Client client);

    /**
     * Возвращает копию списка подключенных клиентов.
     * @return список подключенных клиентов
     */
    List<Client> getSessions();

}
