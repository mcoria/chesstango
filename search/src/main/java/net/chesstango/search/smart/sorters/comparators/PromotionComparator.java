package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.smart.sorters.MoveComparator;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class PromotionComparator implements MoveComparator {

    private MoveComparator next;

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {
        next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(int currentPly, MoveToHashMap moveToZobrist) {
        next.afterSort(currentPly, moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        int result = 0;
        MovePromotion move1Promotion = o1 instanceof MovePromotion promotion ? promotion : null;
        MovePromotion move2Promotion = o2 instanceof MovePromotion promotion ? promotion : null;


        if (move1Promotion != null && move2Promotion != null) {
            return piecePromotionValue(move1Promotion.getPromotion()) - piecePromotionValue(move2Promotion.getPromotion());
        }


        if (move1Promotion != null) {
            return 1;
        } else if (move2Promotion != null) {
            return -1;
        }


        return next.compare(o1, o2);
    }

    private static int piecePromotionValue(Piece piece) {
        return switch (piece) {
            case QUEEN_WHITE, QUEEN_BLACK -> 5;
            case ROOK_WHITE, ROOK_BLACK -> 4;
            case BISHOP_WHITE, BISHOP_BLACK -> 3;
            case KNIGHT_WHITE, KNIGHT_BLACK -> 2;
            default -> throw new RuntimeException("Invalid promotion piece");
        };
    }
}
