package net.chesstango.uci.arena.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chesstango.uci.proxy.ProxyConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class ProxyConfigLoader {
    public static final String ENGINES_JSON = "engines.json";

    public static ProxyConfig loadEngineConfig(String engineName) {

        Optional<ProxyConfig> config = loadFromFile().stream().filter(entry -> entry.getName().equalsIgnoreCase(engineName)).findAny();
        if (config.isEmpty()) {
            throw new RuntimeException("Engine " + engineName + " not found in config file.");
        }
        return config.get();
    }

    protected static List<ProxyConfig> loadFromFile() {
        try {
            InputStream inputStream = ProxyConfig.class.getClassLoader().getResourceAsStream(ENGINES_JSON);

            // reading the files with buffered reader
            assert inputStream != null;
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(inputStreamReader, new TypeReference<List<ProxyConfig>>() {
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
