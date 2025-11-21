package org.Util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.Entity.ClassMethodUrl;
import org.annotation.AnnotationContoller;
import org.annotation.AnnotationMethode;
import org.custom.CustomReflections;

public class CmuUtils {

    public static void saveCmuList(CustomReflections reflections, Map<String, ClassMethodUrl> urlMappings) {

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(AnnotationContoller.class);
        for (Class<?> controller : controllers) {

            for (Method method : controller.getDeclaredMethods()) {

                // Izay methode annotée amle AnnotationMethode iany no alaina
                if (method.isAnnotationPresent(AnnotationMethode.class)) {

                    String url = method.getAnnotation(AnnotationMethode.class).value();

                    ClassMethodUrl cmu = new ClassMethodUrl(controller, method);
                    urlMappings.put(url, cmu);

                    System.out.println("Url : " + url + " methode : " + method.getName() + " Controller : "
                            + controller.getName());
                }

            }
        }
    }

    /*
     * Raha misy "{ }" ao anaty url dia
     * Avadika tableau ilay url par /
     * 
     * [1] = user
     * [2] = {id}
     * [3] = detail
     * 
     * Miditra ny Url dia avadika tableau koa par /
     * Raha mitovy ny longueur dia atao mapping
     * alaina ny position anle {id} ao amle url sauvegardena dia continue rehefa eo
     * aminle position id pour chaque partie de l'url
     * Break raha tsy mitovy dia retourne false
     * 
     * Raha mamerina true ilay fonction teo dia jerena ny anaranle parametre
     * ao amle methode ampitoviana aminy ao anatinle {}
     * dia jerena koa ny position any raha mitovy
     * 
     * Raha mitovy dia alaina ilay valeur ao amle url araka ny position
     */
    public static ClassMethodUrl findMapping(String url, Map<String, ClassMethodUrl> urlMappings,
            HttpServletRequest request) throws Exception {

        // Try templates with {var} placeholders
        for (String key : urlMappings.keySet()) {
            if (!key.contains("{")) {
                continue;
            }

            String[] keyParts = key.split("/");
            String[] urlParts = url.split("/");

            if (keyParts.length != urlParts.length) {
                continue;
            }

            Map<String, String> pathVars = new HashMap<>();
            boolean isMatch = true;

            for (int i = 0; i < keyParts.length; i++) {
                String kp = keyParts[i];
                String up = urlParts[i];

                if (kp.startsWith("{") && kp.endsWith("}")) {
                    String varName = kp.substring(1, kp.length() - 1);
                    System.out.println("hita tao anaty url le varname\n");
                    System.out.println("Varname : " + varName + " url valeur : " + up);
                    
                    pathVars.put(varName, up);
                } else {
                    // Raha misy iray tsy mitovy dia tsy mety zany
                    if (!kp.equals(up)) {
                        System.out.println("Tsy mitovy\n");
                        System.out.println("kp : " + kp + " up : " + up + "\n");

                        isMatch = false;
                        break;
                    }
                }
            }

            if (!isMatch) {
                // this template doesn't match the requested URL, try next mapping
                continue;
            }
            
            // matched: expose extracted path variables and return mapping
            request.setAttribute("pathVars", pathVars);
            return urlMappings.get(key);
        }

        // Exact match first
        if (urlMappings.containsKey(url)) {
            // no path variables
            request.setAttribute("pathVars", new HashMap<String, String>());
            return urlMappings.get(url);
        }

        // No mapping found
        return null;
    }

}
