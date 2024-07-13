package net.chesstango.board.representations.epd;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;

import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class EPD {
    private String id;
    private String text;

    private String fen;

    private String bestMovesString;
    private List<Move> bestMoves;

    private String avoidMovesString;
    private List<Move> avoidMoves;

    private String suppliedMoveString;

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

    public boolean isMoveSuccess(Move bestMove) {
        if (getBestMoves() != null && !getBestMoves().isEmpty()) {
            return getBestMoves().contains(bestMove);
        } else if (getAvoidMoves() != null && !getAvoidMoves().isEmpty()) {
            return !getAvoidMoves().contains(bestMove);
        } else {
            throw new RuntimeException("Undefined expected EPD result");
        }
    }

    public int calculateAccuracyPct(List<Move> moveList) {
        if (!moveList.isEmpty()) {
            long successMovesCounter = moveList.stream().filter(this::isMoveSuccess).count();
            return (int) (successMovesCounter * 100 / moveList.size());
        }
        return 0;
    }
}
