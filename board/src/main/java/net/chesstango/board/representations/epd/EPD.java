package net.chesstango.board.representations.epd;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FEN;

import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class EPD {
    private String text;

    private String id;
    private String c0;
    private String c1;
    private String c2;
    private String c3;
    private String c4;
    private String c5;
    private String c6;
    private String c7;

    //  The halfmove clock and full move counter,
    //  obligatory in Forsyth-Edwards Notation
    //  are replaced by optional hmvc and fmvn operations
    private FEN fenWithoutClocks;

    private String bestMovesStr;
    private List<Move> bestMoves;

    private String avoidMovesStr;
    private List<Move> avoidMoves;

    private String suppliedMoveStr;
    private Move suppliedMove;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        EPD epd = (EPD) object;
        return Objects.equals(getText(), epd.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText());
    }


    @Override
    public String toString() {
        return text != null ? text : new EPDEncoder().encode(this);
    }

    public boolean isMoveSuccess(Move bestMove) {
        if (getBestMoves() != null && !getBestMoves().isEmpty()) {
            return getBestMoves().contains(bestMove);
        } else if (getAvoidMoves() != null && !getAvoidMoves().isEmpty()) {
            return !getAvoidMoves().contains(bestMove);
        } else {
            throw new RuntimeException("Undefined expected EPD result");
        }
    }

    public int calculateAccuracy(List<Move> moveList) {
        if (!moveList.isEmpty()) {
            long successMovesCounter = moveList.stream().filter(this::isMoveSuccess).count();
            return (int) (successMovesCounter * 100 / moveList.size());
        }
        return 0;
    }
}
