package net.chesstango.board.representations.syzygy;

import lombok.Getter;

/**
 * @author Mauricio Coria
 */
@Getter
public enum TableType {
    WDL(".rtbw", 0x5d23e871, 1, 1),
    DTM(".rtbm", 0x88ac504b, 1, 1),
    DTZ(".rtbz", 0xa50c66d7, 1, 1);

    private final String suffix;
    private final int magicNumber;
    private final int ecInfoSizePawnless;
    private final int ecInfoSizePawnful;

    TableType(String suffix, int magicNumber, int ecInfoSizePawnless, int ecInfoSizePawnful) {
        this.suffix = suffix;
        this.magicNumber = magicNumber;
        this.ecInfoSizePawnless = ecInfoSizePawnless;
        this.ecInfoSizePawnful = ecInfoSizePawnful;
    }
}
