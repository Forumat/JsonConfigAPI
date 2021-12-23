## JsonConfigAPI
Simple and easy to use Json Configuration API

### 1 | Add the API to the project
Add the following to your **pom.xml**:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.Forumat</groupId>
    <artifactId>JsonConfigAPI</artifactId>
    <version>5bdf866f</version>
    <scope>compile</scope>
</dependency>
```

Or this to your **build.gradle**:
```
repositories {
  maven { url 'https://jitpack.io' }
}
```
```
dependencies {
  implementation 'com.github.Forumat:JsonConfigAPI:5bdf866f'
}
```

### 2 | Get started
First, you need to create a class, which represents your config file.
Every field in that class, will be a value in the config file.
Now, you have to annotate the whole class with @JsonConfig, and give it the right parameters.
Now, there are two ways to register your config:
#### 1 | Register a single config:
If you only have one or two configs, you can register both of them with the following Method
```
JsonConfigAPI.getInstance().registerConfig(new YourConfig());
```
This method returns an instance from your config.

#### 2 | Register all configs at once:
If you have more configs, it would be better to register them all with one single method:
```
JsonConfigAPI.getInstance().registerConfigsByAnnotation(YourMainClass.class);
```
When you use that method, the API will scan all your classes, and look which of them are annotated with @JsonConfig. Those will be registered automatically.
But it is important, that every JsonConfig is located in the direction of your main class. (like in the example)

It's also possible to edit the config while the program is running. To do this, just edit your config instance, and then call the following method:
```
JsonConfigAPI.getInstance().saveConfig(config);
```

### 3 | Examples
If you don't understand something, try to take a look at the examples, located [here](https://github.com/Forumat/JsonConfigAPI/tree/main/src/main/java/eu/forumat/config/example).

### 4 | Get help
If you still have problems, understanding how to use the api or using it, open an issue on this repository, or visit our [discord server](http://discord.forumat.eu).
