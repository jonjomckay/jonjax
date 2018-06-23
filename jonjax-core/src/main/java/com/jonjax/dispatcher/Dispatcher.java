package com.jonjax.dispatcher;

import com.jonjax.container.Container;
import com.jonjax.routes.NoRoute;
import com.jonjax.routes.Route;
import com.jonjax.routes.RouteTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Dispatcher {
    private final static Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);

    private final Container container;
    private final RouteTable routeTable;

    @Inject
    public Dispatcher(Container container, RouteTable routeTable) {
        this.container = container;
        this.routeTable = routeTable;
    }

    public Object dispatch(String path, String httpMethod) {
        Route route = routeTable.findRoute(path, httpMethod);

        if (route == null || route instanceof NoRoute) {
            LOGGER.debug("Couldn't find a matching route for the path {} and method {}", path, httpMethod);

            throw new NotFoundException("No matching route could be found");
        }

        LOGGER.debug("Found a matching route for the path {} and method {}", path, httpMethod);

        Method method = route.getAction();

        Class<?> declaringClass = method.getDeclaringClass();

        if (container.has(declaringClass) == false) {
            LOGGER.error("No instance of {} could be found or instantiated in the container", declaringClass.getName());

            throw new RuntimeException("No instance of " + declaringClass.getName() + " could be found or instantiated in the container");
        }

        try {
            return method.invoke(container.get(declaringClass));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
