package org.intellij.openapi.testing;

import com.intellij.openapi.application.Application;

public interface MockApplication extends Application {

    void registerComponent(Class componentClass, Object component);

    void removeComponent(Class componentClass);
}
