package org.intellij.openapi.testing.irida;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.util.Key;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.HashMap;
import java.util.Map;

public class MockComponentManager implements ComponentManager {

    private final Map<Class, Object> componentRegistry = new HashMap<Class, Object>();
    private final PicoContainer picoContainer = new DefaultPicoContainer();

    public void registerComponent(Class componentClass, Object component) {
        componentRegistry.put(componentClass, component);
    }

    public void removeComponent(Class componentClass) {
        componentRegistry.remove(componentClass);
    }

    public boolean hasComponent(Class componentClass) {
        return false;
    }

    public <T> T[] getComponents(Class<T> interfaceClass) {
        return null;
    }

    public PicoContainer getPicoContainer() {
        return picoContainer;
    }

    public Class[] getComponentInterfaces() {
        return new Class[0];
    }

    public BaseComponent getComponent(String name) {
        return null;
    }

    public <T> T getComponent(Class<T> interfaceClass) {
        return (T) componentRegistry.get(interfaceClass);
    }

    public <T> T getComponent(Class<T> interfaceClass, T defaultImplementationIfAbsent) {
        return null;
    }

    public <T> T getUserData(Key<T> key) {
        return null;
    }

    public <T> void putUserData(Key<T> key, T value) {}

    public void dispose() {}
}
