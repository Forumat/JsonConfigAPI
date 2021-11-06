package eu.forumat.config;

import eu.forumat.config.annotation.JsonConfig;
import eu.forumat.config.data.JsonConfigData;
import eu.forumat.config.util.JsonFileUtil;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigAPI {

    private static final JsonConfigAPI INSTANCE = new JsonConfigAPI();

    private final JsonFileUtil jsonFileUtil = new JsonFileUtil();
    private final Map<Class<?>, JsonConfigData> registeredConfigs = new HashMap<>();

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
