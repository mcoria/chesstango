
package net.chesstango.board.moves.generators.pseudo.imp;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorEnPassant;
import net.chesstango.board.moves.imp.MoveImp;
import net.chesstango.board.position.SquareBoardReader;
import net.chesstango.board.position.PositionStateReader;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorEnPassantImp implements MoveGeneratorEnPassant {

    private final EnPassantMoveGeneratorBlack pasanteMoveGeneratorBlack = new EnPassantMoveGeneratorBlack();

    private final EnPassantMoveGeneratorWhite pasanteMoveGeneratorWhite = new EnPassantMoveGeneratorWhite();

    @Setter
    private PositionStateReader positionState;

    @Setter
    private SquareBoardReader piecePlacement;


    @Override
    public MovePair generateEnPassantPseudoMoves() {
        Square pawnPasanteSquare = positionState.getEnPassantSquare();
        if (pawnPasanteSquare != null) {
            if (Color.WHITE.equals(positionState.getCurrentTurn())) {
                return pasanteMoveGeneratorWhite.generatePseudoMoves(pawnPasanteSquare);
            } else {
                return pasanteMoveGeneratorBlack.generatePseudoMoves(pawnPasanteSquare);
            }
        }
        return null;
    }


    private class EnPassantMoveGeneratorBlack {
        private final MoveFactory moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryBlack();

        public MovePair generatePseudoMoves(Square pawnPasanteSquare) {
            MovePair moveContainer = new MovePair();
            PiecePositioned origen = null;
            PiecePositioned captura = null;

            Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() + 1);
            if (casilleroPawnIzquirda != null) {
                origen = piecePlacement.getPosition(casilleroPawnIzquirda);
                captura = piecePlacement.getPosition(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
                if (Piece.PAWN_BLACK.equals(origen.getPiece())) {
                    MoveImp move = moveFactoryImp.createCaptureEnPassantPawnMove(origen, piecePlacement.getPosition(pawnPasanteSquare), captura, Cardinal.SurEste);
                    moveContainer.setFirst(move);
                }
            }

            Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() + 1);
            if (casilleroPawnDerecha != null) {
                origen = piecePlacement.getPosition(casilleroPawnDerecha);
                captura = piecePlacement.getPosition(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
                if (Piece.PAWN_BLACK.equals(origen.getPiece())) {
                    MoveImp move = moveFactoryImp.createCaptureEnPassantPawnMove(origen, piecePlacement.getPosition(pawnPasanteSquare), captura, Cardinal.SurOeste);
                    moveContainer.setSecond(move);
                }
            }

            return moveContainer;
        }
    }

    private class EnPassantMoveGeneratorWhite {
        private final MoveFactory moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryWhite();

        public MovePair generatePseudoMoves(Square pawnPasanteSquare) {
            MovePair moveContainer = new MovePair();
            PiecePositioned origen = null;
            PiecePositioned captura = null;

            Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() - 1);
            if (casilleroPawnIzquirda != null) {
                origen = piecePlacement.getPosition(casilleroPawnIzquirda);
                captura = piecePlacement.getPosition(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
                if (Piece.PAWN_WHITE.equals(origen.getPiece())) {
                    MoveImp move = this.moveFactoryImp.createCaptureEnPassantPawnMove(origen, piecePlacement.getPosition(pawnPasanteSquare), captura, Cardinal.NorteEste);
                    moveContainer.setFirst(move);
                }
            }

            Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() - 1);
            if (casilleroPawnDerecha != null) {
                origen = piecePlacement.getPosition(casilleroPawnDerecha);
                captura = piecePlacement.getPosition(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
                if (Piece.PAWN_WHITE.equals(origen.getPiece())) {
                    MoveImp move = moveFactoryImp.createCaptureEnPassantPawnMove(origen, piecePlacement.getPosition(pawnPasanteSquare), captura, Cardinal.NorteOeste);
                    moveContainer.setSecond(move);
                }
            }

            return moveContainer;
        }
    }

}
