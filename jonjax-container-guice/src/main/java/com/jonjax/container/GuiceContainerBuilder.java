package com.jonjax.container;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import javax.inject.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

public class GuiceContainerBuilder implements ContainerBuilder {
    private LinkedHashMap<Class, Class<? extends Provider>> providerClasses = new LinkedHashMap<>();
    private LinkedHashMap<Class, Provider> providerImplementations = new LinkedHashMap<>();

    private final Injector injector;

    public GuiceContainerBuilder() {
        this.injector = Guice.createInjector(new GuiceModule());
    }

    public GuiceContainerBuilder(Injector injector) {
        this.injector = injector.createChildInjector(new GuiceModule());
    }

    public Container build() {
        Module providerModule = new AbstractModule() {
            @Override
            protected void configure() {
                for (Map.Entry<Class, Provider> provider : providerImplementations.entrySet()) {
                    bind(provider.getKey()).toProvider(provider.getValue()).asEagerSingleton();
                }

                for (Map.Entry<Class, Class<? extends Provider>> provider : providerClasses.entrySet()) {
                    bind(provider.getKey()).toProvider(provider.getValue()).asEagerSingleton();
                }
            }
        };

        Injector finalInjector = injector.createChildInjector(providerModule);

        // Finally, we want to register the container inside the container
        Module containerModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Container.class).toInstance(new GuiceContainer(finalInjector));
            }
        };

        return new GuiceContainer(finalInjector.createChildInjector(containerModule));
    }

    public <T> void registerProvider(final Class<T> aClass, final Provider<T> provider) {
        providerImplementations.put(aClass, provider);
    }

    public <T> void registerProvider(Class<T> aClass, Class<? extends Provider<T>> provider) {
        providerClasses.put(aClass, provider);
    }
}
