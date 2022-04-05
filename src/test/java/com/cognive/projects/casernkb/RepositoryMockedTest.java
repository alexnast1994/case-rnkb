package com.cognive.projects.casernkb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.el.MethodNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.camunda.bpm.extension.mockito.CamundaMockito.registerMockInstance;
import static org.mockito.Mockito.when;

@Slf4j
public class RepositoryMockedTest {

    public static void withResource(String resourceName) {
        new RepositoryMockedTest().mockFromResource(resourceName);
    }

    protected void mockFromResource(String resourceName) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode node = readJson(mapper, resourceName);
        JsonNode repos = node.get("repositories");
        if(repos.isArray()) {
            for(int i = 0 ; i < repos.size() ; i++) {
                parseRepository(mapper, repos.get(i));
            }
        } else {
            throw new IllegalArgumentException("repositories should be array");
        }

        log.info("s -> {}", repos);
    }

    private JsonNode readJson(ObjectMapper mapper, String resourceName) {
        if(resourceName == null)
            throw new RuntimeException("resourceName cannot be null");

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        if(stream == null)
            throw new RuntimeException("Cannot find resource with name: " + resourceName);
        try {
            return mapper.readTree(new InputStreamReader(stream));
        } catch (IOException exception) {
            throw new RuntimeException("Cannot parse json", exception);
        }
    }

    private void parseRepository(ObjectMapper mapper, JsonNode node) {
        String className = node.get("className").asText();
        try {
            log.info("Mock class: {}", className);
            Class<?> clazz = this.getClass().getClassLoader().loadClass(className);

            final Object mockedRepository = registerMockInstance(clazz);
            log.info(clazz.getCanonicalName());

            JsonNode methods = node.get("methods");
            if(methods.isArray()) {
                for(int i = 0 ; i < methods.size() ; i++) {
                    parseMethod(mapper, methods.get(i), mockedRepository, clazz);
                }
            } else {
                throw new IllegalArgumentException("methods should be array");
            }
            log.info("Successfully mocked: {}", className);
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Cannot found class: " + className, exception);
        }
    }

    private void parseMethod(ObjectMapper mapper, JsonNode node, Object mockObject, Class<?> clazz) {
        String name = node.get("name").asText();
        JsonNode arguments = node.get("args");
        JsonNode returnedType = node.get("returnedType");
        Class<?>[] argumentsClasses = getArgumentClasses(arguments, name);
        Class<?> returnedClass = getReturnedClass(returnedType);

        log.info("Mock method: {}, args size: {}", name, argumentsClasses.length);

        try {
            Method method = clazz.getMethod(name, argumentsClasses);
            JsonNode objectsNode = node.get("objects");
            parseObjects(mapper, objectsNode, mockObject, method, argumentsClasses, returnedClass);
        } catch (NoSuchMethodException noSuchMethodException) {
            throw new IllegalArgumentException("Method: " + name + " not found", noSuchMethodException);
        } catch (MethodNotFoundException exception) {
            throw new IllegalArgumentException();
        }
    }

    private void parseObjects(ObjectMapper mapper, JsonNode objectsNode, Object mockObject, Method method, Class<?>[] argumentsClasses, Class<?> returnedClass) {
        if(objectsNode.isArray()) {
            for(int i = 0 ; i < objectsNode.size() ; i++) {
                JsonNode objectNode = objectsNode.get(i);
                JsonNode args = objectNode.get("args");
                JsonNode data = objectNode.get("data");

                Object[] objectArgs = getArgumentObjects(mapper, args, argumentsClasses);

                try {
                    Object dataObject = mapper.treeToValue(data, returnedClass);
                    try {
                        when(method.invoke(mockObject, objectArgs)).thenReturn(dataObject);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalArgumentException("Does not access to method", exception);
                    } catch (InvocationTargetException exception) {
                        throw new IllegalArgumentException("Invoke error", exception);
                    } catch (IllegalArgumentException exception) {
                        throw new IllegalArgumentException("Arguments not match", exception);
                    }

                } catch (JsonProcessingException exception) {
                    throw new IllegalArgumentException("Cannot parse data object", exception);
                }
            }
        } else {
            throw new IllegalArgumentException("objects should be array");
        }
    }

    private Class<?>[] getArgumentClasses(JsonNode arguments, String methodName) {
        List<Class<?>> argumentsClasses = new ArrayList<>();
        if(arguments.isArray()) {
            for(int i = 0 ; i < arguments.size() ; i++) {
                String argClassName = arguments.get(i).asText();
                try {
                    Class<?> argClass = this.getClass().getClassLoader().loadClass(argClassName);
                    argumentsClasses.add(argClass);
                } catch (ClassNotFoundException exception) {
                    throw new IllegalArgumentException("Class: " + argClassName + ", for method: " + methodName + ", not found", exception);
                }
            }
        } else {
            throw new IllegalArgumentException("args should be array");
        }
        return argumentsClasses.toArray(new Class<?>[0]);
    }

    private Class<?> getReturnedClass(JsonNode node) {
        try {
            return this.getClass().getClassLoader().loadClass(node.asText());
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Invalid class name: " + node.asText(), ex);
        }
    }

    private Object[] getArgumentObjects(ObjectMapper mapper, JsonNode argsNode, Class<?>[] argumentsClasses) {
        List<Object> objects = new ArrayList<>();

        if(argsNode.isArray()) {
            if(argsNode.size() != argumentsClasses.length) {
                throw new IllegalArgumentException("Invalid size of argument in object and method");
            }

            for(int i = 0 ; i < argsNode.size() ; i++) {
                try {
                    Object argObject = mapper.treeToValue(argsNode.get(i), argumentsClasses[i]);
                    objects.add(argObject);
                } catch (JsonProcessingException exception) {
                    throw new IllegalArgumentException("Cannot parse argument", exception);
                }
            }
        } else {
            throw new IllegalArgumentException("args should be array");
        }

        return objects.toArray(new Object[0]);
    }
}
