package org.intellij.openapi.testing.aurora;

import com.intellij.openapi.application.ApplicationListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Key;
import org.intellij.openapi.testing.MockComponentManager;
import org.intellij.openapi.testing.MockApplication;

public class MockAuroraApplication implements MockApplication {

    private final MockComponentManager componentManager = new MockComponentManager();

    public void registerComponent(Class componentClass, Object component) {
        componentManager.registerComponent(componentClass, component);
    }

    public void removeComponent(Class componentClass) {
        componentManager.removeComponent(componentClass);
    }

    public void runReadAction(Runnable action) {}

    public Object runReadAction(Computable computation) {
        return null;
    }

    public void runWriteAction(Runnable action) {}

    public Object runWriteAction(Computable computation) {
        return null;
    }

    public Object getCurrentWriteAction(Class actionClass) {
        return null;
    }

    public void assertReadAccessAllowed() {}

    public void assertWriteAccessAllowed() {}

    public void assertIsDispatchThread() {}

    public void addApplicationListener(ApplicationListener listener) {}

    public void removeApplicationListener(ApplicationListener listener) {}

    public void saveAll() {}

    public void saveSettings() {}

    public void exit() {}

    public boolean isWriteAccessAllowed() {
        return false;
    }

    public boolean runProcessWithProgressSynchronously(Runnable process, String progressTitle, boolean canBeCanceled,
                                                       Project project) {
        return false;
    }

    public Object getComponent(Class interfaceClass) {
        return componentManager.getComponent(interfaceClass);
    }

    public Class[] getComponentInterfaces() {
        return componentManager.getComponentInterfaces();
    }

    public Object[] getComponents(Class baseInterfaceClass) {
        return componentManager.getComponents(baseInterfaceClass);
    }

    public boolean hasComponent(Class aClass) {
        return componentManager.hasComponent(aClass);
    }

    public Object getComponent(Class interfaceClass, Object defaultImplementationIfAbsent) {
        Object component = componentManager.getComponent(interfaceClass);
        return (component != null) ? component : defaultImplementationIfAbsent;
    }

    public Object getUserData(Key key) {
        return null;
    }

    public void putUserData(Key key, Object value) {}
}
