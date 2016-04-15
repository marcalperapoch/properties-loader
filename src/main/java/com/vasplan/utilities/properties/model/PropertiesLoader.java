package com.vasplan.utilities.properties.model;

import com.vasplan.utilities.properties.utils.Utils;
import org.apache.commons.beanutils.BeanUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by marcal.perapoch on 17/03/2015.
 */
public final class PropertiesLoader {

    private static final String ARRAY_SEPARATOR = ",";
    private static final String ARRAY_INDICATOR_INI = "[";
    private static final String ARRAY_INDICATOR_END = "]";
    private static final String EXCLUDE_META_INF = "META-INF";

    private static PropertiesLoader propertiesLoader;

    private final Reflections reflections;
    private final Set<Class<?>> configuredClasses;
    private final Map<Class<?>, ?> mInstances;
    private final Map<String, Properties> propertiesMap;
    private final Set<String> excludedPropertiesFiles;
    private final String mainPackage;

    private PropertiesLoader(String mainPackage, Set<String> excludedPropertiesFiles) {
        this.mainPackage = mainPackage;
        this.excludedPropertiesFiles = excludedPropertiesFiles;
        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(this.mainPackage))
                .setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner()));
        mInstances = new HashMap<Class<?>, Object>();
        configuredClasses = getConfiguredPropertiesClasses();
        propertiesMap = getAllPropertiesFiles();
    }

    private Set<String> getPropertiesFiles() {
        return reflections.getResources(Pattern.compile(".*\\.properties"));
    }

    private Set<Class<?>> getConfiguredPropertiesClasses() {
        return reflections.getTypesAnnotatedWith(InPropertiesFile.class);
    }

    private boolean isValidPath(String path) {
        return (!excludedPropertiesFiles.contains(path) && !path.startsWith(EXCLUDE_META_INF));
    }

    private Map<String, Properties> getAllPropertiesFiles() {
        Map<String, Properties> properties = new HashMap<String, Properties>();
        for(String path: getPropertiesFiles()) {
            if (isValidPath(path)) {
                properties.put(getClassPathFromProperties(path), loadProperties(path));
            }
        }
        return properties;
    }

    private String getClassPathFromProperties(String path) {
        return path.replace(".properties", "").replace("/", ".");
    }

    private <T> T getClassInstance(Class<T> cl) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (mInstances.containsKey(cl)) {
            return cl.cast(mInstances.get(cl));
        }
        if (configuredClasses.contains(cl)){
            String classToPropertyMap = getPropertiesMapKey(cl);
            if (propertiesMap.containsKey(classToPropertyMap)) {
                T instance = cl.newInstance();
                Properties properties = propertiesMap.get(classToPropertyMap);
                for (Field field: cl.getDeclaredFields()) {
                    setField(instance, field, properties);
                }
                return instance;
            }
            else {
                throw new IllegalArgumentException("Class "+cl+" has not its .properties file in its corresponding path");
            }
        }
        throw new IllegalArgumentException("Class "+cl+" has not the @InPropertiesFile annotation!");
    }


    private Properties loadProperties(String path) {
        Properties prop = new Properties();
        InputStreamReader input = null;

        try {
            InputStream is = PropertiesLoader.class.getClassLoader().getResourceAsStream(path);
            input = new InputStreamReader(is, "UTF8");
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return prop;
    }

    private String getPropertiesMapKey(Class cl) {
        return cl.getName();
    }

    private <T> void setField(T instance, Field field, Properties properties) throws IllegalAccessException, InvocationTargetException {
        String prop = properties.getProperty(field.getName());
        if (prop != null && checkIfList(prop)) {
            Class cl = Utils.getListClass(field.getGenericType().toString());
            BeanUtils.setProperty(instance, field.getName(), getList(prop, cl));
        }
        else {
            BeanUtils.setProperty(instance, field.getName(), prop);
        }
    }


    private boolean checkIfList(String property) {
        return (property.startsWith(ARRAY_INDICATOR_INI) && property.endsWith(ARRAY_INDICATOR_END));
    }

    private <T> List<T> getList(String array, Class<T> elementInListClass) {
        List<T> result = new ArrayList<>();
        array = clearArrayIndicators(array);
        if (array.contains(ARRAY_SEPARATOR)) {
            String[] entries = array.split(ARRAY_SEPARATOR);
            for(String entry: entries) {
                T elem = Utils.toElement(entry, elementInListClass);
                if (elem != null) {
                    result.add(elem);
                }
            }
        }
        return result;
    }

    private String clearArrayIndicators(String array) {
        return array.replace(ARRAY_INDICATOR_INI, "").replace(ARRAY_INDICATOR_END, "");
    }

    private static <T> void initialChecks(Class<T> cl) {
        if (cl == null) {
            throw new IllegalArgumentException("Class can not be null");
        }
        if (propertiesLoader == null) {
            throw new IllegalStateException("Maybe you have forgotten to call initialize(...) method first");
        }
    }

    public static synchronized void initialize(String mainPackage) {
        initialize(mainPackage, new HashSet<String>());
    }

    public static synchronized void initialize(String mainPackage, Set<String> excludedPropertiesFiles) {
        propertiesLoader = new PropertiesLoader(mainPackage, excludedPropertiesFiles);
    }

    public static synchronized boolean isInitialized() {
        return propertiesLoader != null;
    }

    public static synchronized <T> T getInstance(Class<T> cl) throws PropertiesLoaderException {
        Exception exception;
        try {
            initialChecks(cl);
            return propertiesLoader.getClassInstance(cl);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            exception = e;
        } catch (Exception e) {
            exception = e;
        }
        throw new PropertiesLoaderException(exception);
    }
}

