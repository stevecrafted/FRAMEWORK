package org.Util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterMapper {

    public static Object[] mapParameters(Parameter[] methodParameters, HttpServletRequest req, Method method) {
        Object[] methodArgs = new Object[methodParameters.length];

        System.out.println(methodParameters.length + " parametre(s) trouve(s)");
        System.out.println("Mise en place des parametres : ");

        for (int i = 0; i < methodArgs.length; i++) {
            Parameter param = methodParameters[i];
            String paramName = param.getName();
            String paramValue = req.getParameter(paramName);
            
            // Tsy null ito raha itany ao anaty requete ny parametre
            if (paramValue != null) {
                methodArgs[i] = convertParameter(paramValue, param.getType(), paramName);
            } else {
                // Sprint 6 bis
                // Raha tsy hitany ao anaty Parametre anle methode ilay nom ao amle requete dia
                // jerena raha mampiasa anle annotaion RequestParam izy izay
                // System.out.println("Parametre " + paramName + " non trouve dans la requete, verification annotation requestParam");
                if (param.isAnnotationPresent(org.annotation.AnnotationRequestParam.class)) {
                    // System.out.println("AnnotationRequestParam trouvee pour le parametre " + paramName);

                    org.annotation.AnnotationRequestParam requestParamAnnotation = param
                            .getAnnotation(org.annotation.AnnotationRequestParam.class);
                    String requestParamName = requestParamAnnotation.value();
                    String requestParamValue = req.getParameter(requestParamName);

                    if (requestParamValue != null) {
                        methodArgs[i] = convertParameter(requestParamValue, param.getType(), requestParamName);
                    } else {
                        System.out.println("Parametre " + requestParamName + " non trouve dans la requete");
                    }
                } else {
                    System.out.println("Parametre " + paramName + " non trouve dans la requete");
                }
            }
        }

        return methodArgs;
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