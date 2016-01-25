package com.vasplan.utilities.properties.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by marcalperapoch on 2/1/16.
 */
public class Utils {

    private static final Pattern JAVA_LANG_TYPE_PATTERN = Pattern.compile("java.util.List<java.lang.(\\w+)>");

    public static Class getListClass(String fullElementType) {
        Matcher matcher = JAVA_LANG_TYPE_PATTERN.matcher(fullElementType);
        if (!matcher.find()) {
            return null;
        } else {
            String type = matcher.group(1);
            return getJavaLangTypeClass(type);
        }
    }

    private static Class getJavaLangTypeClass(String type) {
        switch (type.toLowerCase()) {
            case "string" :
                return String.class;
            case "integer" :
                return Integer.class;
            case "character" :
                return Character.class;
            case "long" :
                return Long.class;
            case "float" :
                return Float.class;
            case "boolean" :
                return Boolean.class;
            default:
                String msg = String.format("Can not find the corresponding class for %s", type);
                throw new IllegalArgumentException(msg);
        }
    }


    public static <T> T toElement(String entry, Class<T> withClass) {
        try {
            return withClass.getDeclaredConstructor(String.class).newInstance(entry.trim());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
