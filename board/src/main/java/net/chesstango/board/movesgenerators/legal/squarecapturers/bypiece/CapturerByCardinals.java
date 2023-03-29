package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.strategies.RookMoveGenerator;
import net.chesstango.board.position.BoardReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public class CapturerByCardinals implements SquareCapturerByPiece{

    private final BoardReader boardReader;
    private final Color color;
    private final Piece rook;
    private final Piece bishop;
    private final Piece queen;

    public CapturerByCardinals(BoardReader boardReader, Color color) {
        this.boardReader = boardReader;
        this.color = color;
        this.rook = Piece.getRook(color);
        this.bishop = Piece.getBishop(color);
        this.queen = Piece.getQueen(color);
    }

    @Override
    public boolean positionCaptured(Square square) {
        return positionCapturedByDireccion(square, RookMoveGenerator.ROOK_CARDINAL, rook) ||
                positionCapturedByDireccion(square, BishopMoveGenerator.BISHOP_CARDINAL, bishop);
    }

    private boolean positionCapturedByDireccion(Square square, Cardinal[] direcciones, Piece capturer) {
        for (Cardinal cardinal : direcciones) {
            if(positionCapturedByCardinalPieza(square, cardinal, capturer)){
                return true;
            }
        }
        return false;
    }

    private boolean positionCapturedByCardinalPieza(Square square, Cardinal cardinal, Piece capturer) {
        Iterator<PiecePositioned> iterator = boardReader.iterator(new CardinalSquareIterator(square, cardinal));
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            Piece piece = destino.getPiece();
            if (piece == null) {
                continue;
            } else if (queen.equals(piece)) {
                return true;
            } else if (capturer.equals(piece)) {
                return true;
            } else {
                break;
            }
        }
        return false;
    }
}
