package net.chesstango.uci.proxy;




import java.util.List;

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
