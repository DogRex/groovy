package org.intellij.openapi.testing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;

public class MockApplicationManager extends ApplicationManager {

    private static final String AURORA = "org.intellij.openapi.testing.aurora.MockAuroraApplication";
    private static final String PALLADA = "org.intellij.openapi.testing.pallada.MockPalladaApplication";

    private static MockApplication applicationMock = createMockApplication();
    private static Application ourApplicationCopy = ourApplication;

    public static boolean isAurora() {
        try {
            Class.forName("com.intellij.refactoring.move.moveClassesOrPackages.MoveDestination");
            return true;
        } catch (ClassNotFoundException e1) {
            try {
                Class.forName("com.intellij.refactoring.MoveDestination");
                return false;
            } catch (ClassNotFoundException e2) {
                throw new IllegalStateException("Unable to determine the target IDEA release!");
            }
        }
    }

    public static void setApplication(Application application) {
        ourApplication = application;
    }

    public static void reset() {
        if (isAurora()) {
            resetInternalApplication();
        } else {
            stubInternalApplication();
        }
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

    private static void resetInternalApplication() {
        ourApplication = ourApplicationCopy;
    }

    private static MockApplication createMockApplication() {
        try {
            Class clazz = Class.forName((isAurora() ? AURORA : PALLADA));
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
