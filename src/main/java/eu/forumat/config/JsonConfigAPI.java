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
    @Getter private final Map<Class<?>, JsonConfigData> registeredConfigs = new HashMap<>();

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

    public Object registerConfig(Object config) {
        Class<?> configClass = config.getClass();
        if (!configClass.isAnnotationPresent(JsonConfig.class)) {
            throw new IllegalArgumentException("Class " + configClass.getSimpleName() + " is missing a @JsonConfig annotation!");
        }

        JsonConfig configAnnotation = configClass.getAnnotation(JsonConfig.class);
        return registerConfig(new JsonConfigData(
                config,
                new File(configAnnotation.path() + configAnnotation.name())
        ));
    }

    public Object registerConfig(JsonConfigData configData) {
        Class<?> configClass = configData.getConfigObject().getClass();

        registeredConfigs.put(configClass, configData);
        File file = configData.getConfigFile();
        Object config;
        if (file.exists()) {
            config = jsonFileUtil.loadByFile(configClass, file);
        } else {
            config = configData.getConfigObject();
            file.getParentFile().mkdirs();
            jsonFileUtil.saveIntoFile(config, file, true);
        }
        configData.setConfigObject(config);
        return config;
    }

    public void saveConfig(Object config) {
        Class<?> configClass = config.getClass();
        if (!registeredConfigs.containsKey(configClass)) {
            throw new IllegalArgumentException("No config registered for class " + configClass.getSimpleName());
        }

        JsonConfigData configData = registeredConfigs.get(configClass);
        File configFile = configData.getConfigFile();
        if (!configFile.exists()) return;
        configFile.delete();
        jsonFileUtil.saveIntoFile(config, configFile, true);
        configData.setConfigObject(config);
    }

    public <T> T getConfig(Class<?> configClass) {
        return (T) registeredConfigs.get(configClass).getConfigObject();
    }


    public static JsonConfigAPI getInstance() {
        return INSTANCE;
    }
}
