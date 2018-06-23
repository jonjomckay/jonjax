package com.jonjax.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class DefaultContainer implements Container {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultContainer.class);

    private Map<Class, Class<? extends Provider>> classes;
    private Map<Class, Provider> implementations;

    public DefaultContainer(Map<Class, Class<? extends Provider>> classes, Map<Class, Provider> implementations) {
        this.classes = classes;
        this.implementations = implementations;
    }

    @Override
    public <T> T get(Class<T> tClass) {
        if (Container.class.isAssignableFrom(tClass)) {
            return (T) this;
        }

        if (implementations.containsKey(tClass)) {
            return (T) implementations.get(tClass).get();
        }

        if (classes.containsKey(tClass)) {
//            try {


                return (T) get(classes.get(tClass)).get();
//            } catch (IllegalAccessException | InstantiationException e) {
//                LOGGER.error("Unable to instantiate an instance of the provider {}. If your provider's constructor isn't public or parameterless, consider using jonjax-container-guice instead.", tClass.getName());
//
//                throw new RuntimeException(e);
//            }
        }

//        if (classes.containsKey(tClass)) {
//            try {
//                return (T) createFromConstructors(classes.get(tClass)).get();
//            } catch (IllegalAccessException | InstantiationException e) {
//                LOGGER.error("Unable to instantiate an instance of the provider {}. If your provider's constructor isn't public or parameterless, consider using jonjax-container-guice instead.", tClass.getName());
//
//                throw new RuntimeException(e);
//            }
//        }

        return createFromConstructors(tClass);
    }

    @Override
    public boolean has(Class<?> aClass) {
        // If we can create this class from a provider class or implementation, we say we have it in the container
        if (classes.containsKey(aClass)) {
            return true;
        }

        if (implementations.containsKey(aClass)) {
            return true;
        }

        // Or if we're looking for an instance of Container, we say we have it
        if (Container.class.isAssignableFrom(aClass)) {
            return true;
        }

        // Otherwise, we check if we can create the instance manually, using providers and/or parameterless constructors
        // TODO: We can do this at container build time maybe, for eager errors?
        if (findParameterlessConstructor(aClass).isPresent()) {
            return true;
        }

        return findUsableConstructor(aClass).isPresent();
    }

    private <T> T createFromConstructors(Class<T> aClass) {
        // Next, we see if we can create an instance from a parameterless constructor
        Optional<Constructor<?>> parameterlessConstructor = findParameterlessConstructor(aClass);

        if (parameterlessConstructor.isPresent()) {
            try {
                return (T) parameterlessConstructor.get().newInstance();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }

        // Next, we see if we can create an instance based on the constructor parameters
        Optional<Constructor<?>> usableConstructor = findUsableConstructor(aClass);

        if (usableConstructor.isPresent()) {
            Constructor<?> constructor = usableConstructor.get();

            try {
                Object[] parameters = Arrays.stream(constructor.getParameterTypes())
                        .map(this::get)
                        .toArray();

                return (T) constructor.newInstance(parameters);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            return aClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            LOGGER.error("Unable to instantiate an instance of {}. If your constructor isn't public or parameterless, consider using jonjax-container-guice instead.", aClass.getName());

            throw new RuntimeException(e);
        }
    }

    /**
     * Find the parameterless constructor on a given class, if there is one
     * @param aClass The class to look for a parameterless constructor in
     * @return an {@link Optional}, containing a parameterless constructor, if one exists
     */
    private Optional<Constructor<?>> findParameterlessConstructor(Class<?> aClass) {
        Constructor<?>[] constructors = aClass.getConstructors();

        return Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst();
    }

    /**
     * Find a constructor on a given class that we can construct using the instances and providers already in this
     * container
     * @param aClass The class to look for a usable constructor in
     * @return an {@link Optional}, containing a constructor, if one exists that we can successfully instantiate
     */
    private Optional<Constructor<?>> findUsableConstructor(Class<?> aClass) {
        Constructor<?>[] constructors = aClass.getConstructors();

        return Arrays.stream(constructors)
                .filter(constructor -> Arrays.stream(constructor.getParameterTypes()).allMatch(this::has))
                .findFirst();
    }
}
