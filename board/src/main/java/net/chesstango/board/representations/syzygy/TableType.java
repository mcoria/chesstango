package net.chesstango.board.representations.syzygy;

import lombok.Getter;

@Getter
public enum TableType {
    WDL(".rtbw", 0x5d23e871), DTM(".rtbm", 0x88ac504b), DTZ(".rtbz", 0xa50c66d7);

    private final String suffix;
    private final int magicNumber;

    TableType(String suffix, int magicNumber) {
        this.suffix = suffix;
        this.magicNumber = magicNumber;
    }
}
