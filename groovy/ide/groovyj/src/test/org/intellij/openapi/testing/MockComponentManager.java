package org.intellij.openapi.testing;

import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.util.Key;

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

    public Class[] getComponentInterfaces() {
        return new Class[0];
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
