package org.Util.CmuMapperUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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

}
