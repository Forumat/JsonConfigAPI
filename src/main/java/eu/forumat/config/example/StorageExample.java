package eu.forumat.config.example;

import eu.forumat.config.annotation.JsonConfig;
import eu.forumat.config.example.entity.Person;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonConfig(
        path = "C:/Users/Forumat/Desktop/",
        name = "personRegistery.json"
)
public class StorageExample {

    @Getter private final Map<String, List<Person>> countryPopulation = new HashMap<>();

}
