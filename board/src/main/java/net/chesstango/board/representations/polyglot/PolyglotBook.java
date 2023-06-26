package net.chesstango.board.representations.polyglot;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface PolyglotBook {
    void load(Path path);

    List<PolyglotEntry> search(long key);
}
