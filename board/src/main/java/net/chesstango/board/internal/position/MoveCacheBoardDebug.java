package net.chesstango.board.internal.position;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.SquareBoard;


/**
 * @author Mauricio Coria
 */
public class MoveCacheBoardDebug extends MoveCacheBoardImp {

    public MoveCacheBoardDebug() {
        super();
    }

    public void validar(SquareBoard squareBoard) {
        for (int i = 0; i < 64; i++) {
            if (pseudoMoves[i] != null && squareBoard.isEmpty(Square.getSquareByIdx(i))) {
                throw new RuntimeException(String.format("Un casillero de cache contiene movimientos (%s) pero no existe pieza en tablero!!!", Square.getSquareByIdx(i)));
            }
        }

        for (PiecePositioned piecePositioned : squareBoard) {
            if (piecePositioned.getPiece() == null && pseudoMoves[piecePositioned.getSquare().toIdx()] != null) {
                throw new RuntimeException(String.format("Para un casillero de tablero sin pieza (%s) existe movimientos en cache!!!", piecePositioned.getSquare()));
            }
        }

    }

}
