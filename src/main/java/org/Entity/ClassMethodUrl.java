package org.Entity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

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

    public String ExecuteMethode(HttpServletResponse resp) throws IOException {
        try {
            Object controller = this.Class.getDeclaredConstructor().newInstance();
            Object result = this.Method.invoke(controller);

            if (result instanceof String) {
                String viewName = (String) result;
                return viewName;
            } else if (result instanceof ModelView) {
                return ((ModelView)result).getView();
            }

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return "";
    }
}
