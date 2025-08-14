package net.chesstango.board.representations.move;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SimpleMoveEncoder {

    public String encode(Move move) {
        String promotionStr = "";
        if (move instanceof MovePromotion movePromotion) {
            promotionStr = switch (movePromotion.getPromotion()) {
                case ROOK_WHITE, ROOK_BLACK -> "r";
                case KNIGHT_WHITE, KNIGHT_BLACK -> "n";
                case BISHOP_WHITE, BISHOP_BLACK -> "b";
                case QUEEN_WHITE, QUEEN_BLACK -> "q";
                default -> throw new RuntimeException("Invalid promotion " + move);
            };
        }
        return String.format("%s%s%s", move.getFrom().square().toString(), move.getTo().square().toString(), promotionStr);
    }

    public String encodeMoves(List<Move> moves) {
        if (moves == null) {
            return "-";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Move move : moves) {
                sb.append(encode(move));
                sb.append(" ");
            }
            return sb.toString();
        }
    }

}
