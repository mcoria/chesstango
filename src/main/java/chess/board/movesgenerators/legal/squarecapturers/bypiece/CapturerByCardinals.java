package chess.board.movesgenerators.legal.squarecapturers.bypiece;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.bysquare.CardinalSquareIterator;
import chess.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import chess.board.movesgenerators.pseudo.strategies.RookMoveGenerator;
import chess.board.position.PiecePlacementReader;

import java.util.Iterator;

public class CapturerByCardinals implements SquareCapturerByPiece{

    private final PiecePlacementReader piecePlacementReader;
    private final Color color;
    private final Piece rook;
    private final Piece bishop;
    private final Piece queen;

    public CapturerByCardinals(PiecePlacementReader piecePlacementReader, Color color) {
        this.piecePlacementReader = piecePlacementReader;
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
        Iterator<PiecePositioned> iterator = piecePlacementReader.iterator(new CardinalSquareIterator(square, cardinal));
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            Piece piece = destino.getValue();
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
