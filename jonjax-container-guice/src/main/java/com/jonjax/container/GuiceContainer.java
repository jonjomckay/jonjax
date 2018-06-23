package com.jonjax.container;

import com.google.inject.Injector;
import com.google.inject.Key;

public class GuiceContainer implements Container {
    private final Injector injector;

    public GuiceContainer(Injector injector) {
        this.injector = injector;
    }

    public <T> T get(Class<T> tClass) {
        return injector.getInstance(tClass);
    }

    public boolean has(Class<?> aClass) {
        return injector.getBinding(Key.get(aClass)) != null;
    }
}
