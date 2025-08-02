package net.chesstango.engine;

import lombok.Getter;

/**
 * @author Mauricio Coria
 */
public enum ConfigOptions {
    POLYGLOT_PATH("PolyglotPath"),
    SYZYGY_DIRECTORY("SyzygyDirectory");

    @Getter
    final String optionName;

    ConfigOptions(String optionName) {
        this.optionName = optionName;
    }
}
