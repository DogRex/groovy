package org.intellij.openapi.testing.irida;

import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.util.Key;

import org.picocontainer.PicoContainer;

public class MockComponentManager implements ComponentManager {

    private final Map componentRegistry = new HashMap();

    public void registerComponent(Class aClass, Object component) {
        componentRegistry.put(aClass, component);
    }

    public void removeComponent(Class componentClass) {
        componentRegistry.remove(componentClass);
    }

    public boolean hasComponent(Class aClass) {
        return false;
    }

    public Object[] getComponents(Class aClass) {
        return new Object[0];
    }

    public PicoContainer getPicoContainer() {
        return null;
    }

    public Class[] getComponentInterfaces() {
        return new Class[0];
    }

    public BaseComponent getComponent(String name) {
        return null;
    }

    public Object getComponent(Class aClass) {
        return componentRegistry.get(aClass);
    }

    public Object getComponent(Class interfaceClass, Object defaultImplementationIfAbsent) {
        return null;
    }

    public Object getUserData(Key key) {
        return null;
    }

    public void putUserData(Key key, Object value) {}
}
