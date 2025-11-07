package org.Entity;

import java.lang.reflect.Method;

public class ClassMethodUrl {
    Class<?> Class;
    Method Method;

    public ClassMethodUrl(Class<?> Class, Method method) {
        this.Class = Class;
        this.Method = method;
    }

    public Class<?> getMyClass() {
        return this.Class;
    }

    public void setMyClass(Class<?> c) {
        this.Class = c;
    }

    public Method getMyMethod() {
        return this.Method;
    }

    public void SetMethod(Method m) {
        this.Method = m;
    }
}