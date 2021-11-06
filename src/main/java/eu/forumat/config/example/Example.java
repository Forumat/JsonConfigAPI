package eu.forumat.config.example;

import eu.forumat.config.JsonConfigAPI;
import eu.forumat.config.example.entity.Gender;
import eu.forumat.config.example.entity.Person;

import java.util.ArrayList;
import java.util.Arrays;

public class Example {

    public static void main(String[] args) {
        JsonConfigAPI instance = JsonConfigAPI.getInstance();
        instance.registerConfigsByAnnotation(Example.class);

        /*
        CONFIGURATION EXAMPLE
         */
        TestConfig config = instance.getConfig(TestConfig.class);
        System.out.println(config.getTestString() + " | " + config.getTestInteger());

        config.setTestString("Haha, lol");
        instance.saveConfig(config);

        System.out.println(config.getTestString());

        /*
        STORAGING EXAMPLE
         */
        StorageExample storageExample = instance.getConfig(StorageExample.class);

        //Add country (key in the map)
        storageExample.getCountryPopulation().put(
                "Österreich",
                new ArrayList<>(Arrays.asList(
                        new Person("Hans", 18, Gender.MALE, "Schriftsteller"),
                        new Person("Franz", 14, Gender.MALE, "Kellner"),
                        new Person("Marie", 19, Gender.FEMALE, "Buchhalterin")
                ))
        );
        instance.saveConfig(storageExample);
        printCountryPopulations(storageExample);

        Person laura = new Person("Laura", 17, Gender.FEMALE, "Polizistin");
        //Add new person to a country
        storageExample.getCountryPopulation().get("Österreich").add(laura);
        instance.saveConfig(storageExample);
        printCountryPopulations(storageExample);

        //Change name of a person
        storageExample.getCountryPopulation().get("Österreich").stream().filter(person -> person.getName().equals("Laura")).findAny().orElse(laura).setName("Julia");
        instance.saveConfig(storageExample);
        printCountryPopulations(storageExample);
    }

    private static void printCountryPopulations(StorageExample storageExample) {
        storageExample.getCountryPopulation().forEach((country, people) -> {
            System.out.println("» Country - " + country);
            people.forEach(person -> System.out.println("     » " + person));
        });
    }

}
