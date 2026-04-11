package net.chesstango.uci.engine;

import lombok.Getter;

/**
 *
 * @author Mauricio Coria
 */
@Getter
public enum UciOption {
    SYZYGY_PATH("SyzygyPath"),
    POLYGLOT_FILE("PolyglotFile"),
    HASH_SIZE("Hash");

    private final String id;

    UciOption(String id) {
        this.id = id;
    }
}
