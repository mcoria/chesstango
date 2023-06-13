package net.chesstango.board.debug.chess;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.imp.MoveCacheBoardImp;


/**
 * @author Mauricio Coria
 */
public class MoveCacheBoardDebug extends MoveCacheBoardImp {

    public MoveCacheBoardDebug() {
        super();
    }

    public void validar(SquareBoard dummySquareBoard) {
        for (int i = 0; i < 64; i++) {
            if (pseudoMoves[i] != null && dummySquareBoard.isEmpty(Square.getSquareByIdx(i))) {
                throw new RuntimeException(String.format("Un casillero de cache contiene movimientos (%s) pero no existe pieza en tablero!!!", Square.getSquareByIdx(i)));
            }
        }

        for (PiecePositioned piecePositioned : dummySquareBoard) {
            if (piecePositioned.getPiece() == null && pseudoMoves[piecePositioned.getSquare().toIdx()] != null) {
                throw new RuntimeException(String.format("Para un casillero de tablero sin pieza (%s) existe movimientos en cache!!!", piecePositioned.getSquare()));
            }
        }

        validarAffectedByAndAffects();
    }

    private void validarAffectedByAndAffects() {
        //Validate affectedBy[]
        for (Square square : Square.values()) {
            final long squareBitPosition = square.getBitPosition();
            if (pseudoMoves[square.toIdx()] != null) {
                long affectedBySquares = pseudoMoves[square.toIdx()].getAffectedByPositions();
                while (affectedBySquares != 0) {
                    long posicionLng = Long.lowestOneBit(affectedBySquares);
                    int idx = Long.numberOfTrailingZeros(posicionLng);
                    if ((affects[idx] & squareBitPosition) == 0) {
                        throw new RuntimeException("MoveCacheBoard checkConsistence failed");
                    }
                    affectedBySquares &= ~posicionLng;
                }
            }
        }

        //Validate affects[]
        for (Square square : Square.values()) {
            final long squareBitPosition = square.getBitPosition();
            long affectsSquares = affects[square.toIdx()];
            while (affectsSquares != 0) {
                long posicionLng = Long.lowestOneBit(affectsSquares);
                int idx = Long.numberOfTrailingZeros(posicionLng);
                if ((pseudoMoves[idx].getAffectedByPositions() & squareBitPosition) == 0) {
                    throw new RuntimeException("MoveCacheBoard checkConsistence failed");
                }
                affectsSquares &= ~posicionLng;
            }
        }
    }

}
