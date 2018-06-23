package com.jonjax.routes;

import java.lang.reflect.Method;

public class Route {
    private String path;
    private String method;
    private Method action;

    public Route(String path, String method, Method action) {
        this.path = path;
        this.method = method;
        this.action = action;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public Method getAction() {
        return action;
    }
}
