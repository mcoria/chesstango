package net.chesstango.uci.proxy;

/**
 * @author Mauricio Coria
 */
public class SpikeProxy extends ProxyConfig {
    private SpikeProxy(){
        this.setName("SPIKE");
        this.setDirectory("C:\\Java\\projects\\chess\\chess-utils\\arena_3.5.1\\Engines\\Spike");
        this.setExe("C:\\Java\\projects\\chess\\chess-utils\\arena_3.5.1\\Engines\\Spike\\Spike1.4.exe");
    }

    public static final SpikeProxy INSTANCE = new SpikeProxy();
}
