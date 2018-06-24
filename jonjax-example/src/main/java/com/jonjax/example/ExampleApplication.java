package com.jonjax.example;

import com.jonjax.container.ContainerBuilder;
import com.jonjax.container.GuiceContainerBuilder;
import com.jonjax.server.Server;
import com.jonjax.server.undertow.UndertowServer;

import javax.ws.rs.core.Application;

public class ExampleApplication extends Application {
    public static void main(String[] args) {
        ContainerBuilder containerBuilder = new GuiceContainerBuilder();

        Server server = new UndertowServer();
        server.setApplication(new ExampleApplication());
        server.start(containerBuilder);
    }
}
