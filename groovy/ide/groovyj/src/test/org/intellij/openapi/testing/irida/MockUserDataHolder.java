package org.intellij.openapi.testing.irida;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;

public class MockUserDataHolder implements UserDataHolder {

    public /*<T> T*/Object getUserData(Key/*<T>*/ key) {
        return null;
    }

    public /*<T>*/ void putUserData(Key/*<T>*/ key, /*T*/Object value) {

    }
}
