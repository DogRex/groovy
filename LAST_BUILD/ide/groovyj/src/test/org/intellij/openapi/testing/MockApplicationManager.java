package org.intellij.openapi.testing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;

public class MockApplicationManager extends ApplicationManager {

    private static final String IRIDA = "org.intellij.openapi.testing.irida.MockIridaApplication";

    private static MockApplication applicationMock = createMockApplication();

    public static void setApplication(Application application) {
        ourApplication = application;
    }

    public static void reset() {
        stubInternalApplication();
    }

    public static MockApplication getMockApplication() {
        if (ourApplication == null) {
            stubInternalApplication();
        }
        return applicationMock;
    }

    private static void stubInternalApplication() {
        ourApplication = applicationMock;
    }

    private static MockApplication createMockApplication() {
        try {
            Class clazz = Class.forName(IRIDA);
            Constructor constructor = clazz.getConstructor(new Class[0]);
            return (MockApplication) constructor.newInstance(new Object[0]);
        } catch (NumberFormatException e) {
            throw newFactoryRuntimeException(e);
        } catch (InstantiationException e) {
            throw newFactoryRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw newFactoryRuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw newFactoryRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw newFactoryRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw newFactoryRuntimeException(e);
        }
    }

    private static RuntimeException newFactoryRuntimeException(Exception e) {
        return new RuntimeException("Could not load MockApplication", e);
    }
}
