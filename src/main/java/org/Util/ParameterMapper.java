package org.Util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterMapper {

    /**
     * Mappe les paramètres HTTP de la requête vers un tableau d'arguments
     * compatible avec la méthode à invoquer
     * 
     * @param req    La requête HTTP contenant les paramètres
     * @param method La méthode dont on veut mapper les paramètres
     * @return Un tableau d'objets représentant les arguments à passer à la méthode
     */
    public static Object[] mapParameters(Parameter[] methodParameters, HttpServletRequest req, Method method) {
        Object[] methodArgs = new Object[methodParameters.length];

        System.out.println(methodParameters.length + " parametre(s) trouve(s)");
        System.out.println("Mise en place des parametres : ");

        for (int i = 0; i < methodArgs.length; i++) {
            Parameter param = methodParameters[i];
            String paramName = param.getName();
            String paramValue = req.getParameter(paramName);

            if (paramValue != null) {
                methodArgs[i] = convertParameter(paramValue, param.getType(), paramName);
            } else {
                System.out.println("Parametre " + paramName + " non trouve dans la requete");
            }
        }

        return methodArgs;
    }

    /**
     * Convertit une valeur String vers le type attendu
     * 
     * @param value      La valeur à convertir (String)
     * @param targetType Le type cible de la conversion
     * @param paramName  Le nom du paramètre (pour les logs)
     * @return L'objet converti dans le bon type
     */
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
                // Par défaut, on retourne la String
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