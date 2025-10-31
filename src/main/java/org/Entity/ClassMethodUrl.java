package org.Entity;

import java.lang.reflect.Method;

public class ClassMethodUrl {
    Class<?> Class;
    Method Method;
    String Url;

    public ClassMethodUrl(Class<?> Class, Method method, String url) {
        this.Class = Class;
        this.Method = method;
        this.Url = url;
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

    public String getMyUrl() {
        return this.Url;
    }

    public void SetMyUrs(String u) {
        this.Url = u;
    }
}