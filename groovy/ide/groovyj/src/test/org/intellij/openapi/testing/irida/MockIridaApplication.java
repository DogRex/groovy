package org.intellij.openapi.testing.irida;

import java.awt.Component;

import org.intellij.openapi.testing.MockApplication;

import com.intellij.openapi.application.ApplicationListener;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Key;
import com.intellij.peer.PeerFactory;
import com.intellij.psi.EmptySubstitutor;

import org.picocontainer.PicoContainer;

public class MockIridaApplication extends MockUserDataHolder implements MockApplication {

    private final MockComponentManager componentManager = createComponentManager();

    private MockComponentManager createComponentManager() {
        MockComponentManager manager = new MockComponentManager();
        manager.registerComponent(PeerFactory.class, new MockPeerFactory());
        manager.registerComponent(EmptySubstitutor.class, null);
        return manager;
    }

    public void registerComponent(Class componentClass, Object component) {
        componentManager.registerComponent(componentClass, component);
    }

    public void removeComponent(Class componentClass) {
        componentManager.removeComponent(componentClass);
    }

    public void runReadAction(Runnable runnable) {
        runnable.run();
    }

    public void runWriteAction(Runnable runnable) {
        runnable.run();
    }

    public Object getCurrentWriteAction(Class aClass) {
        return null;
    }

    public Object runReadAction(Computable computation) {
        return computation.compute();
    }

    public Object runWriteAction(Computable computation) {
        return computation.compute();
    }

    public boolean isReadAccessAllowed() {
        return false;
    }

    public boolean isWriteAccessAllowed() {
        return false;
    }

    public void assertReadAccessAllowed() {}

    public void assertWriteAccessAllowed() {}

    public void assertIsDispatchThread() {}

    public void addApplicationListener(ApplicationListener applicationListener) {}

    public void removeApplicationListener(ApplicationListener applicationListener) {}

    public void saveAll() {}

    public void saveSettings() {}

    public void exit() {}

    public Object getUserData(Key key) {
        return null;
    }

    public BaseComponent getComponent(String name) {
        return null;
    }

    public Object getComponent(Class aClass) {
        return componentManager.getComponent(aClass);
    }

    public Class[] getComponentInterfaces() {
        return componentManager.getComponentInterfaces();
    }

    public Object[] getComponents(Class aClass) {
        return componentManager.getComponents(aClass);
    }

    public PicoContainer getPicoContainer() {
        return null;
    }

    public boolean hasComponent(Class aClass) {
        return componentManager.hasComponent(aClass);
    }

    public void invokeLater(Runnable runnable) {}

    public void invokeLater(Runnable runnable, ModalityState modalityState) {}

    public void invokeAndWait(Runnable runnable, ModalityState modalityState) {}

    public ModalityState getCurrentModalityState() {
        return null;
    }

    public ModalityState getModalityStateForComponent(Component component) {
        return null;
    }

    public ModalityState getDefaultModalityState() {
        return null;
    }

    public ModalityState getNoneModalityState() {
        return null;
    }

    public long getStartTime() {
        return 0;
    }

    public long getIdleTime() {
        return 0;
    }

    public boolean isUnitTestMode() {
        return false;
    }

    public boolean isDispatchThread() {
        return false;
    }

    public Object getComponent(Class aClass, Object o) {
        return null;
    }

    public boolean runProcessWithProgressSynchronously(Runnable runnable, String s, boolean b, Project project) {
        return false;
    }
}
