package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCaptureEnPassant;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.smart.sorters.MoveComparator;

/**
 * @author Mauricio Coria
 */
public class MvvLvaComparator implements MoveComparator {

    @Setter
    @Getter
    private MoveComparator next;

    // MVV_VLA[victim][attacker]
    private static int[][] MVVLVA_MATRIX = {
            {
                    0, 0, 0, 0, 0, 0, 0
            }, // victim K, attacker K, Q, R, B, N, P, None

            {
                    50, 51, 52, 53, 54, 55, 0
            }, // victim Q, attacker K, Q, R, B, N, P, None

            {
                    40, 41, 42, 43, 44, 45, 0
            }, // victim R, attacker K, Q, R, B, N, P, None

            {
                    30, 31, 32, 33, 34, 35, 0
            }, // victim B, attacker K, Q, R, B, N, P, None

            {
                    20, 21, 22, 23, 24, 25, 0
            }, // victim N, attacker K, Q, R, B, N, P, None

            {
                    10, 11, 12, 13, 14, 15, 0
            }, // victim P, attacker K, Q, R, B, N, P, None

            {
                    0, 0, 0, 0, 0, 0, 0
            }  // victim None, attacker K, Q, R, B, N, P, None
    };

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {
        next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(MoveToHashMap moveToZobrist) {
        next.afterSort(moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        int value1 = getValue(o1);
        int value2 = getValue(o2);

        int result = Integer.compare(value1, value2);

        return result != 0 ? result : next.compare(o1, o2);
    }

    private int getValue(Move move) {
        int attackerIdx = getIdx(move.getFrom().getPiece());
        int victimIdx = 0;
        if (move instanceof MoveCaptureEnPassant moveCaptureEnPassant) {
            victimIdx = getIdx(moveCaptureEnPassant.getCapture().getPiece());
        } else {
            victimIdx = getIdx(move.getTo().getPiece());
        }

        return MVVLVA_MATRIX[victimIdx][attackerIdx];
    }

    private int getIdx(Piece piece) {
        return switch (piece) {
            case KING_WHITE, KING_BLACK -> 0;
            case QUEEN_WHITE, QUEEN_BLACK -> 1;
            case ROOK_WHITE, ROOK_BLACK -> 2;
            case BISHOP_WHITE, BISHOP_BLACK -> 3;
            case KNIGHT_WHITE, KNIGHT_BLACK -> 4;
            case PAWN_WHITE, PAWN_BLACK -> 5;
        };
    }
}
