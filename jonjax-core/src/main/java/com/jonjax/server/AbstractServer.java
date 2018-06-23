package com.jonjax.server;

import com.jonjax.container.ContainerBuilder;
import com.jonjax.container.DefaultContainerBuilder;
import com.jonjax.providers.RouteTableProvider;
import com.jonjax.providers.ScanResultProvider;
import com.jonjax.routes.RouteTable;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

import javax.ws.rs.core.Application;

public abstract class AbstractServer implements Server {
    protected Application application;
    protected String host = "localhost";
    protected int port = 8080;

    public Server setApplication(Application application) {
        this.application = application;

        return this;
    }

    public Server setHost(String host) {
        this.host = host;

        return this;
    }

    public Server setPort(int port) {
        this.port = port;

        return this;
    }

    @Override
    public void start() {
        start(new DefaultContainerBuilder());
    }

    @Override
    public void start(final ContainerBuilder containerBuilder) {
        containerBuilder.registerProvider(ScanResult.class, ScanResultProvider.class);
        containerBuilder.registerProvider(RouteTable.class, RouteTableProvider.class);

        start(containerBuilder.build());
    }
}
