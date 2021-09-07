package ru.finex.gs.service;

import ru.finex.gs.model.Client;

import java.util.List;

/**
 * @author m0nster.mind
 */
public interface ClientService {

    void addSession(Client client);
    void removeSession(Client client);

    /**
     * Возвращает копию списка подключенных клиентов.
     * @return список подключенных клиентов
     */
    List<Client> getSessions();

}
