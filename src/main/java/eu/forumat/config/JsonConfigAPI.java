package eu.forumat.config;

import eu.forumat.config.annotation.JsonConfig;
import eu.forumat.config.data.JsonConfigData;
import eu.forumat.config.util.JsonFileUtil;
import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigAPI {

    private static final JsonConfigAPI INSTANCE = new JsonConfigAPI();

    private final JsonFileUtil jsonFileUtil = new JsonFileUtil();
    private final Map<Class<?>, JsonConfigData> registeredConfigs = new HashMap<>();

    public void registerConfigsByAnnotation(Class<?> mainClass) {
        Reflections reflections = new Reflections(mainClass.getPackage().getName());
        ClasspathHelper.forPackage(mainClass.getPackage().getName(), mainClass.getClassLoader()).forEach(reflections::scan);

        reflections.getTypesAnnotatedWith(JsonConfig.class).forEach(configClass -> {
            try {
                registerConfig(configClass.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                System.err.println("Invalid constructor for " + configClass.getSimpleName());
            }
        });
    }

    public void registerConfig(Object config) {
        Class<?> configClass = config.getClass();
        if (!configClass.isAnnotationPresent(JsonConfig.class)) {
            throw new IllegalArgumentException("Class " + configClass.getSimpleName() + " is missing a @JsonConfig annotation!");
        }

        JsonConfig configAnnotation = configClass.getAnnotation(JsonConfig.class);
        registerConfig(new JsonConfigData(
                config,
                new File(configAnnotation.path() + configAnnotation.name())
        ));
    }

    public void registerConfig(JsonConfigData configData) {
        Class<?> configClass = configData.getConfigObject().getClass();
        if (!configClass.isAnnotationPresent(JsonConfig.class)) {
            throw new IllegalArgumentException("Class " + configClass.getSimpleName() + " is missing a @JsonConfig annotation!");
        }

        registeredConfigs.put(configClass, configData);
        File file = configData.getConfigFile();
        if (file.exists()) {
            Object config = jsonFileUtil.loadByFile(configClass, file);
            configData.setConfigObject(config);
        } else {
            Object jsonConfig = configData.getConfigObject();
            file.getParentFile().mkdirs();
            jsonFileUtil.saveIntoFile(jsonConfig, file, true);
            configData.setConfigObject(jsonConfig);
        }
    }


    public static JsonConfigAPI getInstance() {
        return INSTANCE;
    }
}
