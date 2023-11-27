package com.server.ptitFood.config.dsrouting;

import com.server.ptitFood.config.dsrouting.ClientDao;

public class ClientService {
    private final ClientDao clientDao;

    public ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public String getClientName(ClientDatabase clientDb) {
        ClientDatabaseContextHolder.set(clientDb);
        String clientName = this.clientDao.getClientName();
        ClientDatabaseContextHolder.clear();
        return clientName;
    }
}
