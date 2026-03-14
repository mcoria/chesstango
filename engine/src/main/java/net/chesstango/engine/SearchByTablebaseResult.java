package net.chesstango.engine;

import net.chesstango.board.moves.Move;

import static net.chesstango.piazzolla.syzygy.Syzygy.*;

/**
 * @author Mauricio Coria
 */
public record SearchByTablebaseResult(Move move, int syzygyResult, long timeSearching) implements SearchResponse {

    @Override
    public long getTimeSearching() {
        return timeSearching;
    }

    @Override
    public String toString() {
        return wdlToString(syzygyResult);
    }

    public static String wdlToString(int syzygyResult) {
        int tb_result = TB_GET_WDL(syzygyResult);
        return switch (tb_result) {
            case TB_WIN -> "TB_WIN";
            case TB_CURSED_WIN -> "TB_CURSED_WIN";
            case TB_DRAW -> "TB_DRAW";
            case TB_BLESSED_LOSS -> "TB_BLESSED_LOSS";
            case TB_LOSS -> "TB_LOSS";
            default -> "UNKNOWN";
        };
    }
}
