package net.chesstango.board.representations;

import net.chesstango.board.moves.Move;

import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class EpdEntry {
    public String id;
    public String text;

    public String fen;
    public String bestMovesString;
    public List<Move> bestMoves;

    public String avoidMovesString;
    public List<Move> avoidMoves;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        EpdEntry epdEntry = (EpdEntry) object;
        return Objects.equals(text, epdEntry.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    public boolean isMoveSuccess(Move bestMove) {
        if (bestMoves != null && !bestMoves.isEmpty()) {
            return bestMoves.contains(bestMove);
        } else if (avoidMoves != null && !avoidMoves.isEmpty()) {
            return !avoidMoves.contains(bestMove);
        } else {
            throw new RuntimeException("Undefined expected EPD result");
        }
    }
}
