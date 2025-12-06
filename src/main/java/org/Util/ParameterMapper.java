package org.Util;

import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

public class ParameterMapper {

    public static Object[] mapParameters(Parameter[] methodParameters, HttpServletRequest req, Method method) {
        Object[] methodArgs = new Object[methodParameters.length];

        System.out.println(methodParameters.length + " parametre(s) trouve(s)");
        System.out.println("Mise en place des parametres : ");

        for (int i = 0; i < methodArgs.length; i++) {
            Parameter param = methodParameters[i];
            String paramName = param.getName();
            Class<?> paramType = param.getType();

            /*
             * Sprint 8,
             * Raha misy Map<String, String> ao anaty Parametre anle Controlleur
             * jerena aloha raha Map String string le izy raha de type map le parametre
             * Raha oui dia micrée Map vaovao de alefa ao daolo le nom param sy value
             * alefa any am method args aveo dia lasa ho azy
             * 
             * Methode mitsatsaka :)
             */
            if (Map.class.isAssignableFrom(paramType)) {
                if (isMapStringString(param)) {
                    Map<String, String> dataMap = extractAllParameters(req);
                    methodArgs[i] = dataMap;
                    System.out.println("Parametre " + paramName + " (Map<String, String>) mis en place avec "
                            + dataMap.size() + " entree(s)");
                    continue;
                } else {
                    System.out.println(
                            "Type Map non supporte pour " + paramName + " (seul Map<String, String> est accepte)");
                    methodArgs[i] = new HashMap<String, String>();
                    continue;
                }
            }

            /*
             * Sprint 6
             * Raha ita any anaty requet ?id=zavatra na post avy any am formulaire dia alefa
             * direct any am
             * parametre le valeur
             */
            String paramValue = req.getParameter(paramName);
            if (paramValue != null) {
                methodArgs[i] = convertParameter(paramValue, param.getType(), paramName);
                continue;
            }

            /*
             * Sprint 6 Ter, raha toa ka /{id}/url...
             * dia ao omena direct anle
             * 
             * Efa sauvegarde ato aminy "req.getAttribute("pathVars")
             * daolo ny nom anle parametre sy ny valeur any
             */
            Object attr = req.getAttribute("pathVars");
            if (attr instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> pathVars = (Map<String, String>) attr;
                String raw = pathVars.get(paramName);
                if (raw != null) {
                    methodArgs[i] = convertParameter(raw, param.getType(), paramName);
                    continue;
                }
            }

            /*
             * Sprint 6 bis
             * Raha mampiasa annotation @RequestParam dia matchena ilay valeur
             */
            if (param.isAnnotationPresent(org.annotation.AnnotationRequestParam.class)) {
                org.annotation.AnnotationRequestParam requestParamAnnotation = param
                        .getAnnotation(org.annotation.AnnotationRequestParam.class);
                String requestParamName = requestParamAnnotation.value();
                String requestParamValue = req.getParameter(requestParamName);

                if (requestParamValue != null) {
                    methodArgs[i] = convertParameter(requestParamValue, param.getType(), requestParamName);
                    continue;
                } else {
                    System.out.println("Parametre " + requestParamName + " non trouve dans la requete");
                }
            } else {
                System.out.println("Parametre " + paramName + " non trouve dans la requete");
            }
        }

        return methodArgs;
    }

    /*
     * Methode ahafantarana oe String String ve ny parametre
     * iray
     */
    private static boolean isMapStringString(Parameter parameter) {
        Type genericType = parameter.getParameterizedType();

        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments.length == 2
                    && typeArguments[0] == String.class
                    && typeArguments[1] == String.class) {
                return true;
            }
        }

        return false;
    }
    
    /*
     * Methode manala any parametre ao amle Controlleur Map String,String
     * Micréer Map vaovao aminy le Map taloha
     */
    private static Map<String, String> extractAllParameters(HttpServletRequest req) {
        Map<String, String> dataMap = new HashMap<>();

        // 1. Récupérer tous les paramètres de la requête (GET ou POST)
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = req.getParameter(paramName);
            dataMap.put(paramName, paramValue);
            System.out.println("  - Ajout parametre : " + paramName + " = " + paramValue);
        }

        // 2. Récupérer les variables de path (ex: /user/{id}/detail)
        Object pathVarsAttr = req.getAttribute("pathVars");
        if (pathVarsAttr instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> pathVars = (Map<String, String>) pathVarsAttr;
            for (Map.Entry<String, String> entry : pathVars.entrySet()) {
                dataMap.put(entry.getKey(), entry.getValue());
                System.out.println("  - Ajout variable path : " + entry.getKey() + " = " + entry.getValue());
            }
        }

        return dataMap;
    }

    private static Object convertParameter(String value, Class<?> targetType, String paramName) {
        try {
            Object convertedValue;

            if (targetType == int.class || targetType == Integer.class) {
                convertedValue = Integer.parseInt(value);

            } else if (targetType == double.class || targetType == Double.class) {
                convertedValue = Double.parseDouble(value);

            } else if (targetType == float.class || targetType == Float.class) {
                convertedValue = Float.parseFloat(value);

            } else if (targetType == long.class || targetType == Long.class) {
                convertedValue = Long.parseLong(value);

            } else if (targetType == boolean.class || targetType == Boolean.class) {
                convertedValue = Boolean.parseBoolean(value);

            } else if (targetType == String.class) {
                convertedValue = value;

            } else {
                System.out.println("Type non supporte pour " + paramName + ", retour en String");
                convertedValue = value;
            }

            System.out.println("Parametre " + paramName + " mis en place avec la valeur : " + value);
            return convertedValue;

        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion pour le parametre " + paramName +
                    " avec la valeur '" + value + "'");
            throw new IllegalArgumentException(
                    "Impossible de convertir le parametre " + paramName + " en " + targetType.getSimpleName(), e);
        }
    }
}