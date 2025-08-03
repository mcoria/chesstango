package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mauricio Coria
 */
public class Config {
    @Getter
    @Setter
    public String polyglotFile;

    @Getter
    @Setter
    public String syzygyDirectory;
}
