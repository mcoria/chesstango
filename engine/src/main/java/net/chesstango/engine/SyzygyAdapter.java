package net.chesstango.engine;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.position.PositionReader;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.piazzolla.syzygy.SyzygyPosition;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

import static net.chesstango.piazzolla.syzygy.Syzygy.*;


/**
 * Adapter class that interfaces with Syzygy tablebases for chess endgame evaluation.
 * This class implements the EndGameTableBase interface and provides access to Syzygy
 * endgame tablebase probing functionality. It can evaluate positions with a small
 * number of pieces remaining on the board using perfect endgame knowledge.
 *
 * @author Mauricio Coria
 */
class SyzygyAdapter implements EndGameTableBase {
    private final Syzygy syzygy;
    private final int tbLargest;
    private final SyzygyPosition syzygyPosition;

    private Game game;

    /**
     * Constructs a new SyzygyAdapter with the specified Syzygy tablebase.
     *
     * @param syzygy The Syzygy tablebase instance to be used for position evaluation
     */
    SyzygyAdapter(Syzygy syzygy) {
        this.syzygy = syzygy;
        this.tbLargest = syzygy.tb_largest();
        this.syzygyPosition = new SyzygyPosition();
    }

    /**
     * Checks if the current position can be evaluated using the Syzygy tablebase.
     * The position must have fewer pieces than or equal to the largest supported
     * tablebase size.
     *
     * @return true if the position can be probed in the tablebase, false otherwise
     */
    @Override
    public boolean isProbeAvailable() {
        return tbLargest >= 3 && tbLargest >= Long.bitCount(game.getPosition().getAllPositions());
    }

    /**
     * Evaluates the current chess position using the Syzygy tablebase.
     * Returns a score indicating whether the position is winning, losing, or drawing.
     * The evaluation takes into account blessed/cursed wins/losses.
     *
     * @return The evaluation score from the tablebase perspective
     * @throws IllegalStateException if the position cannot be evaluated
     */
    @Override
    public int evaluate() {
        SyzygyPosition syzygyPosition = bindSyzygyPosition();
        int res = syzygy.tb_probe_wdl(syzygyPosition);
        if (res != Syzygy.TB_RESULT_FAILED) {
            int wdl = Syzygy.TB_GET_WDL(res);
            if (syzygyPosition.isTurn()) {
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


    /**
     * Sets the current game context for tablebase evaluation.
     *
     * @param game The game instance containing the position to be evaluated
     */
    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Converts the current chess position to Syzygy tablebase format.
     * Maps the piece positions and game state to the internal representation
     * used by the Syzygy tablebase.
     *
     * @return A SyzygyPosition object representing the current position
     */
    SyzygyPosition bindSyzygyPosition() {
        PositionReader position = game.getPosition();

        syzygyPosition.setPawns(position.getPawnPositions());
        syzygyPosition.setKnights(position.getKnightPositions());
        syzygyPosition.setBishops(position.getBishopPositions());
        syzygyPosition.setQueens(position.getQueenPositions());
        syzygyPosition.setRooks(position.getRookPositions());
        syzygyPosition.setKings(position.getKingPositions());
        syzygyPosition.setWhite(position.getPositions(Color.WHITE));
        syzygyPosition.setBlack(position.getPositions(Color.BLACK));
        syzygyPosition.setRule50((byte) position.getHalfMoveClock());

        Square ep = position.getEnPassantSquare();
        syzygyPosition.setEp(ep == null ? 0 : (byte) ep.idx());

        syzygyPosition.setTurn(Color.WHITE.equals(position.getCurrentTurn()));

        return syzygyPosition;
    }
}
