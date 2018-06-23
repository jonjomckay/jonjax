package com.jonjax.providers;

import com.jonjax.routes.Route;
import com.jonjax.routes.RouteTable;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.List;

public class RouteTableProvider implements Provider<RouteTable> {
    private final static Logger LOGGER = LoggerFactory.getLogger(RouteTableProvider.class);

    private final ScanResult scanResult;

    @Inject
    public RouteTableProvider(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    @Override
    public RouteTable get() {
        RouteTable routeTable = new RouteTable();

        List<Class<?>> classes = scanResult.classNamesToClassRefs(scanResult.getNamesOfClassesWithAnnotation(Path.class));
        for (Class<?> klass : classes) {
            Path classPath = klass.getAnnotation(Path.class);

            // Get all the methods annotated with @Path
            List<Class<?>> classesWithPathMethods = scanResult.classNamesToClassRefs(scanResult.getNamesOfClassesWithMethodAnnotation(Path.class));
            for (Class<?> classWithPathMethod : classesWithPathMethods) {
                for (Method method : classWithPathMethod.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Path.class)) {
                        String path;

                        if (classPath.value().equals("/")) {
                            path = method.getAnnotation(Path.class).value();
                        } else {
                            path = classPath.value() + method.getAnnotation(Path.class).value();
                        }

                        // TODO: LOL no
                        String httpMethod = null;

                        if (method.isAnnotationPresent(GET.class)) {
                            httpMethod = "GET";
                        }

                        if (httpMethod != null) {
                            routeTable.addRoute(new Route(path, httpMethod, method));

                            LOGGER.info("Registered route with the path {} and the method {}", path, httpMethod);
                        }
                    }
                }
            }
        }

        return routeTable;
    }
}
