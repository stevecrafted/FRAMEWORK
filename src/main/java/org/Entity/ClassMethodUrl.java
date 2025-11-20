package org.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest; 

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

    // Mi execute anle methode raha string no averiny
    public String ExecuteMethodeString(Object[]argumentArgs) throws Exception {
        try {
            Object controller = this.Class.getDeclaredConstructor().newInstance();
            Object result = this.Method.invoke(controller, argumentArgs);

            if (result instanceof String) {
                String viewName = (String) result;
                return viewName;
            } else {
                throw new Exception("le type de retour doit etre de type String");
            }

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return "";
    }

    // Mi execute anle methode raha model view no averiny
    public String ExecuteMethodeModelView(HttpServletRequest req, Object[]argumentArgs) throws Exception {
        try {
            Object controller = this.Class.getDeclaredConstructor().newInstance();
            Object result = this.Method.invoke(controller, argumentArgs);

            if (result instanceof ModelView) {
                ModelView modelViewResultExecution = (ModelView) result;
                Map<String, Object> resultExecution = modelViewResultExecution.getAllAttributes();

                if (req == null) {
                    throw new Exception("L'objet req est null");
                }

                for (Map.Entry<String, Object> resultatModelView : resultExecution.entrySet()) {
                    req.setAttribute(resultatModelView.getKey(), resultatModelView.getValue());
                }

                return ((ModelView) result).getView();
            } else {
                throw new Exception("le type de retour doit etre de type model view");
            }

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return "";
    }
}
