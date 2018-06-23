package com.jonjax.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

public class DefaultContainerBuilder implements ContainerBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContainerBuilder.class);

    private Map<Class, Class<? extends Provider>> providerClasses = new HashMap<>();
    private Map<Class, Provider> providerImplementations = new HashMap<>();

    @Override
    public Container build() {
        LOGGER.warn("You are using the default container implementation, which isn't optimized for performance or usability. Please consider using a different implementation!");

        return new DefaultContainer(providerClasses, providerImplementations);
    }

    @Override
    public <T> void registerProvider(Class<T> aClass, Provider<T> provider) {
        providerImplementations.put(aClass, provider);
    }

    @Override
    public <T> void registerProvider(Class<T> aClass, Class<? extends Provider<T>> provider) {
        providerClasses.put(aClass, provider);
    }
}
