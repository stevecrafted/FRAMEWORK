package org.Util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.Entity.ClassMethodUrl;
import org.SprintDeux.AnnotationMethode;
import org.SprintDeuxBis.AnnotationContoller;
import org.custom.CustomReflections;
import org.reflections.Reflections;

public class CmuUtils {

    public static void saveCmuList(CustomReflections reflections, List<ClassMethodUrl> listClassMethodUrl) {

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(AnnotationContoller.class);
        for (Class<?> controller : controllers) {

            for (Method method : controller.getDeclaredMethods()) {

                // Izay methode annotée amle AnnotationMethode iany no alaina
                if (method.isAnnotationPresent(AnnotationMethode.class)) {

                    String url = method.getAnnotation(AnnotationMethode.class).value();
                    ClassMethodUrl cmu = new ClassMethodUrl(controller, method, url);
                    listClassMethodUrl.add(cmu);
                    System.out.println("Url : " + url + " methode : " + method.getName() + " Controller : "
                            + controller.getName());
                }

            }
        }
    }
}
