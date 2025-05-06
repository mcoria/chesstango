package net.chesstango.board.moves.containers;

import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;

/**
 * @author Mauricio Coria
 */
public class MoveToHashMap {
    private static final int ARRAY_SIZE = 64 * 64;
    private static final int PROMOTION_ARRAY_SIZE = 8 * 8 * 4;
    private final long[] regularMoveHashArray;
    private final int[] regularMoveSessionArray;
    private final long[] promotionMoveHashArray;
    private final int[] promotionMoveSessionArray;
    private int currentSessionId;

    public MoveToHashMap() {
        this.regularMoveSessionArray = new int[ARRAY_SIZE];
        this.regularMoveHashArray = new long[ARRAY_SIZE];

        this.promotionMoveHashArray = new long[PROMOTION_ARRAY_SIZE];
        this.promotionMoveSessionArray = new int[PROMOTION_ARRAY_SIZE];

        this.currentSessionId = Integer.MIN_VALUE;
    }

    public void write(Move move, long hash) {
        if (move instanceof MovePromotion movePromotion) {
            writeForPromotionMove(movePromotion, hash);
        } else {
            writeForRegularMove(move, hash);
        }
    }

    public long read(Move move) {
        if (move instanceof MovePromotion movePromotion) {
            return getHashForPromotionMove(movePromotion);
        } else {
            return getHashForRegularMove(move);
        }
    }

    private long getHashForRegularMove(Move move) {
        int idx = move.getFrom().getSquare().idx() * 64 + move.getTo().getSquare().idx();
        int hashSession = regularMoveSessionArray[idx];
        return hashSession == currentSessionId ? regularMoveHashArray[idx] : 0;
    }

    private long getHashForPromotionMove(MovePromotion movePromotion) {
        int idx = movePromotion.getFrom().getSquare().getFile() * 8 * 4 + movePromotion.getTo().getSquare().getFile() * 4 + getPieceOffset(movePromotion.getPromotion());
        int hashSession = promotionMoveSessionArray[idx];
        return hashSession == currentSessionId ? promotionMoveHashArray[idx] : 0;
    }

    private void writeForRegularMove(Move move, long hash) {
        int idx = move.getFrom().getSquare().idx() * 64 + move.getTo().getSquare().idx();
        regularMoveSessionArray[idx] = currentSessionId;
        regularMoveHashArray[idx] = hash;
    }

    private void writeForPromotionMove(MovePromotion movePromotion, long hash) {
        int idx = movePromotion.getFrom().getSquare().getFile() * 8 * 4 + movePromotion.getTo().getSquare().getFile() * 4 + getPieceOffset(movePromotion.getPromotion());
        promotionMoveSessionArray[idx] = currentSessionId;
        promotionMoveHashArray[idx] = hash;
    }

    public void clear() {
        if (currentSessionId < Integer.MAX_VALUE) {
            currentSessionId++;
        } else {
            currentSessionId = Integer.MIN_VALUE;
        }
    }

    private static int getPieceOffset(Piece promotion) {
        return switch (promotion) {
            case KNIGHT_WHITE, KNIGHT_BLACK -> 0;
            case BISHOP_WHITE, BISHOP_BLACK -> 1;
            case ROOK_WHITE, ROOK_BLACK -> 2;
            case QUEEN_WHITE, QUEEN_BLACK -> 3;
            default -> throw new RuntimeException("Wrong promotion");
        };
    }
}
