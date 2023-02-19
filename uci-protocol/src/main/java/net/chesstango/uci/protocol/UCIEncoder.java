package net.chesstango.uci.protocol;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;

public class UCIEncoder {

    public String encode(Move move) {
        String promotionStr = "";
        if(move instanceof MovePromotion){
            MovePromotion movePromotion = (MovePromotion) move;
            switch (movePromotion.getPromotion()) {
                case ROOK_WHITE:
                case ROOK_BLACK:
                    promotionStr = "r";
                    break;
                case KNIGHT_WHITE:
                case KNIGHT_BLACK:
                    promotionStr = "n";
                    break;
                case BISHOP_WHITE:
                case BISHOP_BLACK:
                    promotionStr = "b";
                    break;
                case QUEEN_WHITE:
                case QUEEN_BLACK:
                    promotionStr = "q";
                    break;
                default:
                    throw new RuntimeException("Invalid promotion " + move);
            }
        }
        return move.getFrom().getSquare().toString() + move.getTo().getSquare().toString() + promotionStr;
    }
}
