package com.jonjax.routes;

import java.lang.reflect.Method;

public class NoRoute extends Route {
    public NoRoute(String path, String method, Method action) {
        super(path, method, action);
    }
}
