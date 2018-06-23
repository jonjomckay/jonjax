package com.jonjax.server.undertow;

import com.jonjax.container.Container;
import com.jonjax.server.AbstractServer;
import com.jonjax.dispatcher.DispatcherServlet;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletInfo;
import io.undertow.servlet.util.ImmediateInstanceHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

public class UndertowServer extends AbstractServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(UndertowServer.class);

    @Override
    public void start(final Container container) {
        ServletInfo dispatcherServlet = Servlets.servlet("DispatcherServlet", DispatcherServlet.class, () -> {
            return new ImmediateInstanceHandle<>(container.get(DispatcherServlet.class));
        });

        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(this.application.getClass().getClassLoader())
                .setContextPath("/")
                .setDeploymentName("application")
                .addServlet(dispatcherServlet.addMapping("/*"));

        DeploymentManager manager = Servlets.defaultContainer()
                .addDeployment(servletBuilder);

        manager.deploy();

        try {
            PathHandler handler = Handlers.path()
                    .addPrefixPath("/", manager.start());

            Undertow undertow = Undertow.builder()
                    .addHttpListener(this.port, this.host)
                    .setHandler(handler)
                    .build();

            undertow.start();

            LOGGER.info("Server started and listening on {}:{}", host, port);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
