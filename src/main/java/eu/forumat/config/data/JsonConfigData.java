package eu.forumat.config.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;

@Data
@AllArgsConstructor
public class JsonConfigData {

    @Setter private Object configObject;
    private final File configFile;

}
