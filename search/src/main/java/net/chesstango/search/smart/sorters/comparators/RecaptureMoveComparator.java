package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Game;
import net.chesstango.board.GameStateReader;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class RecaptureMoveComparator implements MoveComparator, SearchByCycleListener {

    private Game game;
    private Move previousMove;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void beforeSort() {
        GameStateReader previousState = this.game.getState().getPreviousState();
        previousMove = previousState.getSelectedMove();
    }

    @Override
    public void afterSort() {
    }

    @Override
    public int compare(Move o1, Move o2) {
        if (Objects.nonNull(previousMove) && !previousMove.isQuiet()) {
            Square previousMoveToSquare = previousMove.getTo().getSquare();
            Square o1ToSquare = o1.getTo().getSquare();
            Square o2ToSquare = o2.getTo().getSquare();

            if (o1ToSquare.equals(o2ToSquare)) {
                // Ambos mueven al mismo casillero de destino, comparar por pieza
                if (Objects.equals(previousMoveToSquare, o1ToSquare)) {
                    Piece o1Piece = o1.getFrom().getPiece();
                    Piece o2Piece = o2.getFrom().getPiece();
                    if (!o1Piece.equals(o2Piece)) {
                        return getMovePieceValue(o1Piece) < getMovePieceValue(o2Piece) ? 1 : -1;
                    }
                }
            } else {
                // Mueven hacia casilleros ditintos, la comparacion es sencilla
                if (Objects.equals(previousMoveToSquare, o1ToSquare)) {
                    return 1;
                } else if (Objects.equals(previousMoveToSquare, o2ToSquare)) {
                    return -1;
                }
            }
        }
        return 0;
    }


    private static int getMovePieceValue(Piece piece) {
        return switch (piece) {
            case QUEEN_WHITE, QUEEN_BLACK -> 5;
            case KNIGHT_WHITE, KNIGHT_BLACK -> 4;
            case BISHOP_WHITE, BISHOP_BLACK -> 3;
            case ROOK_WHITE, ROOK_BLACK -> 2;
            case PAWN_WHITE, PAWN_BLACK -> 1;
            case KING_WHITE, KING_BLACK -> 0;
            default -> throw new RuntimeException("Invalid promotion piece");
        };
    }

}
