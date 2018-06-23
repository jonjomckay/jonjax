package com.jonjax.container;

import javax.inject.Provider;

public interface ContainerBuilder {
    Container build();
    <T> void registerProvider(Class<T> aClass, Provider<T> provider);
    <T> void registerProvider(Class<T> aClass, Class<? extends Provider<T>> provider);
}
