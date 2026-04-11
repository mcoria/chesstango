package net.chesstango.engine;

/**
 * @author Mauricio Corial
 */
public interface TangoOptions {
    void setPolyglotFile(String polyglotFile);

    void setSyzygyPath(String syzygyPath);

    void setHashSize(int hashSize);
}
