package com.jonjax.routes;

import java.util.HashSet;
import java.util.Set;

public class RouteTable {
    private Set<Route> routes = new HashSet<>();

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public Route findRoute(String path, String method) {
        for (Route route : routes) {
            if (route.getPath().equalsIgnoreCase(path)) {
                if (route.getMethod().equalsIgnoreCase(method)) {
                    return route;
                }
            }
        }

        return new NoRoute(path, method, null);
    }
}
