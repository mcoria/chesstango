package net.chesstango.uci.proxy;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class ProxyConfig {
    private String name;
    private String directory;
    private String exe;
    private String params;
}
