package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;

import java.util.Objects;

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

    private String syzygyPath;

    private Integer hashSize;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Config config)) return false;
        return Objects.equals(syncSearch, config.syncSearch)
                && Objects.equals(polyglotFile, config.polyglotFile)
                && Objects.equals(syzygyPath, config.syzygyPath)
                && Objects.equals(hashSize, config.hashSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(syncSearch, polyglotFile, syzygyPath);
    }
}
