package clevertec.config;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ConfigurationLoader {

    /**
     * The default file path for the configuration file.
     */
    private final String configFilePath = "src/main/resources/application.yml";

    /**
     * Loads the configuration from the YAML file.
     *
     * @return A map containing the key-value pairs loaded from the configuration file.
     * @throws IOException If there is an error reading the configuration file.
     */
    public Map<String, Object> loadConfig() throws IOException {
        try (InputStream input = new FileInputStream(configFilePath)) {
            Yaml yaml = new Yaml();
            return yaml.load(input);
        }
    }
}
