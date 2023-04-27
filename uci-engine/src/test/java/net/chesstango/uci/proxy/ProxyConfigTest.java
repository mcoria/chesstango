package net.chesstango.uci.proxy;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class ProxyConfigTest {

    @Test
    public void testReadConfigs(){
        List<ProxyConfig> configs = ProxyConfig.loadFromFile();
        assertTrue(configs.size() > 0);
    }
}
