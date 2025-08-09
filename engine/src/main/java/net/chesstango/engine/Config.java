package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Search;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class Config {
    private boolean syncSearch;

    private Search search;

    private String polyglotFile;

    private String syzygyDirectory;
}
