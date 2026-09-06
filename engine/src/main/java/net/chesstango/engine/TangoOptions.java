package net.chesstango.engine;

import java.nio.file.Path;
import java.util.Set;

/**
 * @author Mauricio Corial
 */
public interface TangoOptions {
    void setPolyglotFile(Path polyglotFile);

    void setSyzygyPath(Set<Path> syzygyDirs);

    void setHashSize(int hashSizeMB);
}
