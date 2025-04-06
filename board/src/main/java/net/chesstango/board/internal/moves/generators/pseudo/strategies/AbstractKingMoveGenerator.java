package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.bypiece.KingPositionsSquareIterator;
import net.chesstango.board.moves.factories.KingMoveFactory;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorCastling;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.KingSquareReader;
import net.chesstango.board.position.PositionStateReader;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 */
@Setter
public abstract class AbstractKingMoveGenerator extends AbstractJumpMoveGenerator implements MoveGeneratorCastling {

    protected KingMoveFactory moveFactory;

    protected PositionStateReader positionState;

    protected KingSquareReader kingSquare;

    public AbstractKingMoveGenerator(Color color) {
        super(color);
    }

    protected boolean validateCastlingQueen(
            final Square origen,
            final PiecePositioned king,
            final PiecePositioned rook,
            final Square casilleroIntermedioRook,
            final Square casilleroDestinoKing,
            final Square casilleroIntermedioKing) {
        if (king.getSquare().equals(origen)) {                                                                        //El king se encuentra en su lugar
            if (rook.getPiece().equals(squareBoard.getPiece(rook.getSquare()))) {                                        //La rook se encuentra en su lugar
                //El casillero intermedio KING esta vacio
                return squareBoard.isEmpty(casilleroIntermedioRook)                                                    //El casillero intermedio ROOK esta vacio
                        && squareBoard.isEmpty(casilleroDestinoKing)                                                        //El casillero destino KING esta vacio
                        && squareBoard.isEmpty(casilleroIntermedioKing);
            }
        }
        return false;
    }

    protected boolean validateCastlingKing(
            final Square origen,
            final PiecePositioned king,
            final PiecePositioned rook,
            final Square casilleroDestinoKing,
            final Square casilleroIntermedioKing) {
        if (king.getSquare().equals(origen)) {                                                                        //El king se encuentra en su lugar
            if (rook.getPiece().equals(squareBoard.getPiece(rook.getSquare()))) {                                        //La rook se encuentra en su lugar
                //El casillero intermedio KING esta vacio
                return squareBoard.isEmpty(casilleroDestinoKing)                                                        //El casillero destino KING esta vacio
                        && squareBoard.isEmpty(casilleroIntermedioKing);
            }
        }
        return false;
    }

    @Override
    protected PseudoMove createSimpleMove(PiecePositioned from, PiecePositioned to) {
        return this.moveFactory.createSimpleKingMove(from, to);
    }

    @Override
    protected PseudoMove createCaptureMove(PiecePositioned from, PiecePositioned to) {
        return this.moveFactory.createCaptureKingMove(from, to);
    }

    @Override
    protected Iterator<Square> getSquareIterator(Square fromSquare) {
        return new KingPositionsSquareIterator(fromSquare);
    }

}
