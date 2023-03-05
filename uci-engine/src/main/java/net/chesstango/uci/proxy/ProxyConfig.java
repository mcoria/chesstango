package net.chesstango.uci.proxy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class ProxyConfig {
    private String name;
    private String directory;
    private String exe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getExe() {
        return exe;
    }

    public void setExe(String exe) {
        this.exe = exe;
    }

    public static ProxyConfig loadEngineConfig(String engineName) {
        Optional<ProxyConfig> config = loadFromFile().stream().filter(entry -> entry.getName().equalsIgnoreCase(engineName)).findAny();
        if (!config.isPresent()) {
            throw new RuntimeException("Engine " + engineName + " not found in config file.");
        }
        return config.get();
    }

    protected static List<ProxyConfig> loadFromFile() {
        try {
            InputStream inputStream = ProxyConfig.class.getClassLoader().getResourceAsStream("engines.json");

            // reading the files with buffered reader
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(inputStreamReader, new TypeReference<List<ProxyConfig>>() {
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
