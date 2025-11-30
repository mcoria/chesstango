package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class Config {
    private Boolean syncSearch;

    private Search search;

    private Evaluator evaluator;

    private String polyglotFile;

    private String syzygyDirectory;
}
