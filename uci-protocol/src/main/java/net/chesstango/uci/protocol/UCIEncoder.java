package net.chesstango.uci.protocol;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.moves.MovePromotion;

/**
 * @author Mauricio Coria
 */
public class UCIEncoder {

    public static String encode(Move move) {
        String promotionStr = "";
        if (move instanceof MovePromotion) {
            MovePromotion movePromotion = (MovePromotion) move;
            promotionStr = switch (movePromotion.getPromotion()) {
                case ROOK_WHITE, ROOK_BLACK -> "r";
                case KNIGHT_WHITE, KNIGHT_BLACK -> "n";
                case BISHOP_WHITE, BISHOP_BLACK -> "b";
                case QUEEN_WHITE, QUEEN_BLACK -> "q";
                default -> throw new RuntimeException("Invalid promotion " + move);
            };
        }
        return move.getFrom().getSquare().toString() + move.getTo().getSquare().toString() + promotionStr;
    }

    public static Move selectMove(MoveContainerReader possibleMoves, String moveStr) {
        for (Move move : possibleMoves) {
            String encodedMoveStr = encode(move);
            if (encodedMoveStr.equals(moveStr.toLowerCase())) {
                return move;
            }
        }
        return null;
    }
}
