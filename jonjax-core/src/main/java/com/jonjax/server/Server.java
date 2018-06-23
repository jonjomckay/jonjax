package com.jonjax.server;

import com.jonjax.container.Container;
import com.jonjax.container.ContainerBuilder;

import javax.ws.rs.core.Application;

public interface Server {
    Server setApplication(Application application);
    Server setHost(String host);
    Server setPort(int port);

    void start();
    void start(Container container);
    void start(ContainerBuilder containerBuilder);
}
