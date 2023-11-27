package com.server.ptitFood.config.dsrouting;

import org.springframework.util.Assert;

public class ClientDatabaseContextHolder {
    private static final ThreadLocal<ClientDatabase> CONTEXT = new ThreadLocal<>();

    public static void set(ClientDatabase clientDatabase) {
        Assert.notNull(clientDatabase, "clientDatabase cannot be null");
        CONTEXT.set(clientDatabase);
    }

    public static ClientDatabase getClientDatabase() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
