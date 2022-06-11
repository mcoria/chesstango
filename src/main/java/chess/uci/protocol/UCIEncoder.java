package chess.uci.protocol;

import chess.board.moves.Move;
import chess.board.moves.MovePromotion;

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
                    promotionStr = "k";
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
        return move.getFrom().getKey().toString() + move.getTo().getKey().toString() + promotionStr;
    }
}
