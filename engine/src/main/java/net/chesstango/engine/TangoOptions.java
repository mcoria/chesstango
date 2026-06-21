package net.chesstango.engine;

import java.nio.file.Path;

/**
 * @author Mauricio Corial
 */
public interface TangoOptions {
    void setPolyglotFile(Path polyglotFile);

    void setSyzygyPath(String syzygyPath);

    void setHashSize(int hashSizeMB);
}
