package com.jonjax.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {
    private final static Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Dispatcher dispatcher;

    @Inject
    public DispatcherServlet(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void init(ServletConfig servletConfig) {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Object result = dispatcher.dispatch(request.getPathInfo(), request.getMethod());

            response.getWriter().print(result);
        } catch (NotFoundException e) {
            response.sendError(e.getResponse().getStatus(), e.getMessage());
        }
    }
}
