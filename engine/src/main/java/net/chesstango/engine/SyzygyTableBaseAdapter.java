package net.chesstango.engine;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.position.PositionReader;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.piazzolla.syzygy.SyzygyPosition;
import net.chesstango.search.smart.features.egtb.EndGameTableBase;

import static net.chesstango.piazzolla.syzygy.Syzygy.*;

/**
 * @author Mauricio Coria
 */
public class SyzygyTableBaseAdapter implements EndGameTableBase {
    private final Syzygy syzygy;
    private final int tbLargest;
    private final SyzygyPosition syzygyPosition;

    private Game game;

    public SyzygyTableBaseAdapter(Syzygy syzygy) {
        this.syzygy = syzygy;
        this.tbLargest = syzygy.tb_largest();
        this.syzygyPosition = new SyzygyPosition();
    }

    @Override
    public boolean isProbeAvailable() {
        return tbLargest >= 3 && tbLargest >= Long.bitCount(game.getPosition().getAllPositions());
    }

    @Override
    public int evaluate() {
        bindSyzygyPosition();
        int res = syzygy.tb_probe_wdl(syzygyPosition);
        if (res != Syzygy.TB_RESULT_FAILED) {
            int wdl = Syzygy.TB_GET_WDL(res);
            if (Color.WHITE.equals(game.getPosition().getCurrentTurn())) {
                return switch (wdl) {
                    case TB_WIN -> Evaluator.WHITE_WON;
                    case TB_CURSED_WIN -> Evaluator.WHITE_WON - 1;
                    case TB_DRAW -> 0;
                    case TB_BLESSED_LOSS -> Evaluator.BLACK_WON + 1;
                    case TB_LOSS -> Evaluator.BLACK_WON;
                    default -> throw new IllegalStateException("Unexpected value: " + wdl);
                };
            } else {
                return switch (wdl) {
                    case TB_WIN -> Evaluator.BLACK_WON;
                    case TB_CURSED_WIN -> Evaluator.BLACK_WON + 1;
                    case TB_DRAW -> 0;
                    case TB_BLESSED_LOSS -> Evaluator.WHITE_WON - 1;
                    case TB_LOSS -> Evaluator.WHITE_WON;
                    default -> throw new IllegalStateException("Unexpected value: " + wdl);
                };
            }
        }
        throw new IllegalStateException("Failed to evaluate position");
    }


    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    private void bindSyzygyPosition() {
        PositionReader position = game.getPosition();
        syzygyPosition.setPawns(position.getPawnPositions());
        syzygyPosition.setKnights(position.getKnightPositions());
        syzygyPosition.setBishops(position.getBishopPositions());
        syzygyPosition.setQueens(position.getQueenPositions());
        syzygyPosition.setRooks(position.getRookPositions());
        syzygyPosition.setKings(position.getKingPositions());
        syzygyPosition.setWhite(position.getPositions(Color.WHITE));
        syzygyPosition.setBlack(position.getPositions(Color.BLACK));
        syzygyPosition.setTurn(Color.WHITE.equals(position.getCurrentTurn()));
    }
}
