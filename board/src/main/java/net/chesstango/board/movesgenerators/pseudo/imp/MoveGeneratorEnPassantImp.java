/**
 *
 */
package net.chesstango.board.movesgenerators.pseudo.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.MoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorEnPassant;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorEnPassantImp implements MoveGeneratorEnPassant {

    private final EnPassantMoveGeneratorBlack pasanteMoveGeneratorBlack = new EnPassantMoveGeneratorBlack();
    private final EnPassantMoveGeneratorWhite pasanteMoveGeneratorWhite = new EnPassantMoveGeneratorWhite();

    private PositionState positionState;

    private PiecePlacementReader piecePlacement;


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
        private final MoveFactory moveFactoryImp = MoveFactories.getDefaultMoveFactoryBlack();

        public MovePair generatePseudoMoves(Square pawnPasanteSquare) {
            MovePair moveContainer = new MovePair();
            PiecePositioned origen = null;
            PiecePositioned captura = null;

            Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() + 1);
            if (casilleroPawnIzquirda != null) {
                origen = piecePlacement.getPosicion(casilleroPawnIzquirda);
                captura = piecePlacement.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
                if (Piece.PAWN_BLACK.equals(origen.getPiece())) {
                    Move move = moveFactoryImp.createCaptureEnPassant(origen, piecePlacement.getPosicion(pawnPasanteSquare), Cardinal.SurOeste, captura);
                    moveContainer.setFirst(move);
                }
            }

            Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() + 1);
            if (casilleroPawnDerecha != null) {
                origen = piecePlacement.getPosicion(casilleroPawnDerecha);
                captura = piecePlacement.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
                if (Piece.PAWN_BLACK.equals(origen.getPiece())) {
                    Move move = moveFactoryImp.createCaptureEnPassant(origen, piecePlacement.getPosicion(pawnPasanteSquare), Cardinal.SurEste, captura);
                    moveContainer.setSecond(move);
                }
            }

            return moveContainer;
        }
    }

    private class EnPassantMoveGeneratorWhite {
        private final MoveFactory moveFactoryImp = MoveFactories.getDefaultMoveFactoryWhite();

        public MovePair generatePseudoMoves(Square pawnPasanteSquare) {
            MovePair moveContainer = new MovePair();
            PiecePositioned origen = null;
            PiecePositioned captura = null;

            Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() - 1);
            if (casilleroPawnIzquirda != null) {
                origen = piecePlacement.getPosicion(casilleroPawnIzquirda);
                captura = piecePlacement.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
                if (Piece.PAWN_WHITE.equals(origen.getPiece())) {
                    Move move = this.moveFactoryImp.createCaptureEnPassant(origen, piecePlacement.getPosicion(pawnPasanteSquare), Cardinal.NorteOeste, captura);
                    moveContainer.setFirst(move);
                }
            }

            Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() - 1);
            if (casilleroPawnDerecha != null) {
                origen = piecePlacement.getPosicion(casilleroPawnDerecha);
                captura = piecePlacement.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
                if (Piece.PAWN_WHITE.equals(origen.getPiece())) {
                    Move move = moveFactoryImp.createCaptureEnPassant(origen, piecePlacement.getPosicion(pawnPasanteSquare), Cardinal.NorteEste, captura);
                    moveContainer.setSecond(move);
                }
            }

            return moveContainer;
        }
    }


    public void setBoardState(PositionState positionState) {
        this.positionState = positionState;
    }


    public void setPiecePlacement(PiecePlacementReader piecePlacement) {
        this.piecePlacement = piecePlacement;
    }

}
