package net.chesstango.uci.proxy;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class ProxyConfigTest {

    @Test
    public void testReadConfigs(){
        List<ProxyConfig> configs = ProxyConfig.loadFromFile();
        Assert.assertTrue(configs.size() > 0);
    }
}
