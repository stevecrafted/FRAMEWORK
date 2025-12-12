package org.Util.CmuMapperUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class Utils {

    public static String extractFieldName(String s) {
        int idx = s.indexOf('[');
        return (idx == -1) ? s : s.substring(0, idx);
    }

    public static Integer extractIndex(String s) {
        int start = s.indexOf('[');
        int end = s.indexOf(']');
        if (start == -1)
            return null;
        return Integer.parseInt(s.substring(start + 1, end));
    }

    public static Class<?> getListElementType(Field field) {
        ParameterizedType pt = (ParameterizedType) field.getGenericType();
        return (Class<?>) pt.getActualTypeArguments()[0];
    }

    public static Object convert(String value, Class<?> fieldType) {
        if (value == null)
            return null;

        if (fieldType == String.class) {
            return value;
        }
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value);
        }
        if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(value);
        }
        if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(value);
        }
        if (fieldType == float.class || fieldType == Float.class) {
            return Float.parseFloat(value);
        }
        if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (fieldType == LocalDate.class) {
            return LocalDate.parse(value);
        }
        if (fieldType.isEnum()) {
            return Enum.valueOf((Class<Enum>) fieldType, value);
        }

        throw new IllegalArgumentException("Type non supporté : " + fieldType.getName());
    }

    public static Object convertParameter(String value, Class<?> targetType, String paramName) {
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

    

    public static boolean isPrimitive(Class<?> type) {
        return (type.isPrimitive() || type.isInstance(String.class) || Number.class.isAssignableFrom(type)
                || type == Boolean.class);
    }

    /*
     * Methode ahafantarana oe String String ve ny parametre
     * iray
     */
    public static boolean isMapStringString(Parameter parameter) {
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

}
