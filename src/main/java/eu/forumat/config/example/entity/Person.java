package eu.forumat.config.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Person {

    @Setter private String name;
    private final int age;
    private final Gender gender;
    private final String job;

    @Override
    public String toString() {
        return getName() + " (" + age + ") " + gender.name() + " [" + job + "]";
    }
}
