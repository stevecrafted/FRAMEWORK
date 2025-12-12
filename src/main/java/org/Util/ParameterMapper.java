package org.Util;

import javax.servlet.http.HttpServletRequest;

import org.annotation.AnnotationRequestParam;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

public class ParameterMapper {

    public static Object[] mapParameters(Parameter[] methodParameters, HttpServletRequest req, Method method)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
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
            if (param.isAnnotationPresent(AnnotationRequestParam.class)) {
                AnnotationRequestParam requestParamAnnotation = param.getAnnotation(AnnotationRequestParam.class);
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

            /*
             * Sprint 8 bis
             * sprintHuitBis(Employe e)
             * requete: e.name = zavatra
             * e.dept[0].name = zavatra
             *
             * Afaka tonga dia jerena ao am paramType raha tsy objet
             * Primitive le parametre (string, int, Boolean, etc.)
             * Verifiena raha misy "." ao anatinle requete (Post na Get)
             * 
             * Raha misy dia :
             * alaina ny eo alohanle "."
             * 
             * - jerena raha misy mitovy anarana amle izy ao anaty
             * methode anle controlleur
             * - Micréer instance anle Objet iny
             * - setena alaina ny ao ariana ".name"
             * 
             * - jerena raha misy attribut otraniny ao amle Objet
             * setena
             * 
             * Iny objet iny no alefa ao aminy controlleur
             */
            Map<String, String[]> parametre = req.getParameterMap();
            /*
             * renvoie un truc de type
             * "e.name" = ["John"],
             * "e.departement[0].name" = ["IT"],
             * "e.departement[1].name" = ["HR"],
             * 
             * "id" = [12]
             * "d.name" = ["Info"]
             */
            if (!isPrimitive(paramType)) {
                // stocker les paramètres qui concernent cet objet
                Map<String, String> filtered = new HashMap<>();

                for (Map.Entry<String, String[]> entry : parametre.entrySet()) {
                    String parameterKey = entry.getKey();
                    String[] parameterValue = entry.getValue();

                    System.out.println("parameterKey : " + parameterKey);
                    System.out.println("paramName : " + paramName);

                    if (parameterKey.startsWith(paramName + ".")) {
                        filtered.put(parameterKey.substring(paramName.length() + 1), parameterValue[0]);

                        for (String value : parameterValue) {
                            System.out.println("value : " + value);
                        }
                    }

                }

                if (!filtered.isEmpty()) {
                    System.out.println("-> Paramètre objet détecté : " + paramName);
                    System.out.println("   " + filtered.size() + " champs trouvés pour " + paramName);

                    Object instance = populateObject(paramType, filtered);
                    methodArgs[i] = instance;
                    continue;
                }
            }

        }

        return methodArgs;

    }

    private static Object populateObject(Class<?> clazz, Map<String, String> values)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        try {
            /*
             * Eto mahazo Employee de le map String, String :
             * "name" = ["John"],
             * "departement[0].name" = ["IT"],
             * "departement[1].name" = ["HR"],
             * 
             * * "departement[1].manager.parent.name" = ["Papi drac"],
             */
            Object isntance = clazz.getDeclaredConstructor().newInstance();

            for (Map.Entry<String, String> entry : values.entrySet()) {
                String paramKey = entry.getKey();
                String paramValue = entry.getValue();

                // Eto izy raha ohatra ka e.name = drac
                if (!paramKey.contains(".")) {
                    try {
                        var field = clazz.getDeclaredField(paramKey);
                        field.setAccessible(true);
                        field.set(isntance, paramValue);
                    } catch (NoSuchFieldException e) {
                        System.out.println("Champ inconnu: " + paramKey);
                    }
                } else {
                    // Raha misy oe e.departement[0].name = transylvanie
                    // Raha misy oe e.departement[0].manager.parent.name = papi drac
                    var field = clazz.getDeclaredField(paramKey);
                    Class<?> underClassType = field.getType();

                    if (!underClassType.isPrimitive()) {
                        Object underInstance = underClassType.getDeclaredConstructor().newInstance();


                    }
                }
            }

            return isntance;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du mapping objet", e);
        }
    }

    private static boolean isPrimitive(Class<?> type) {
        return (type.isPrimitive() || type.isInstance(String.class) || Number.class.isAssignableFrom(type)
                || type == Boolean.class);
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
     * Methode manala any valeur anle parametre ao anaty requetes
     * Dia par rapport amininy valeur reny no icréena anle Map String, String vaovao
     * 
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