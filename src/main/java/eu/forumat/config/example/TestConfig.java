package eu.forumat.config.example;

import eu.forumat.config.annotation.JsonConfig;
import lombok.Getter;
import lombok.Setter;

@JsonConfig(
        path = "C:/Users/Forumat/Desktop/",
        name = "config_lol.json"
)
@Getter
public class TestConfig {

    @Setter private String testString = "lel";
    private int testInteger = 21;

}
