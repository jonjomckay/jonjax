package com.jonjax.container;

public interface Container {
    <T> T get(Class<T> tClass) throws InstantiationException;
    boolean has(Class<?> aClass);
//    void registerProviders(Class<?> aClass, Provider provider);


}
