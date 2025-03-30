package net.chesstango.board.moves.generators.legal.imp.nocheck;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveCommand;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.squarecapturers.CardinalSquareCaptured;
import net.chesstango.board.moves.generators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.KingSquare;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.SquareBoard;

/**
 * Este filtro se utiliza cuando el jugador actual no se encuentra en jaque
 *
 * @author Mauricio Coria
 */
public class NoCheckLegalMoveFilter implements LegalMoveFilter {

    protected final SquareBoard squareBoard;
    protected final KingSquare kingCacheBoard;
    protected final BitBoard bitBoard;
    protected final PositionStateReader positionState;

    protected final FullScanSquareCaptured fullScanSquareCapturer;
    protected final CardinalSquareCaptured cardinalSquareCapturer;

    public NoCheckLegalMoveFilter(SquareBoard squareBoard, KingSquare kingCacheBoard, BitBoard bitBoard, PositionStateReader positionState) {
        this.squareBoard = squareBoard;
        this.kingCacheBoard = kingCacheBoard;
        this.bitBoard = bitBoard;
        this.positionState = positionState;
        this.fullScanSquareCapturer = new FullScanSquareCaptured(squareBoard, bitBoard);
        this.cardinalSquareCapturer = new CardinalSquareCaptured(squareBoard, bitBoard);
    }

    @Override
    //TODO: deberiamos crear un filtro especifico para EnPassant?
    //      solo movimientos EnPassant terminan siendo filtrados aca,
    //      el resto de los movimiento notking son filtrados por el NoCheckLegalMoveGenerator
    /**
     *  Este metodo sirve para filtrar movimientos que no son de rey.
     *  Dado que no se encuentra en jaque, no pregunta por jaque de knight; king o pawn
     */
    public boolean isLegalMove(Move move, MoveCommand command) {
        boolean result = false;

        final Color currentTurn = positionState.getCurrentTurn();

        command.doMove(this.squareBoard);
        command.doMove(this.bitBoard);

        if (!cardinalSquareCapturer.isCaptured(currentTurn.oppositeColor(), kingCacheBoard.getKingSquare(currentTurn))) {
            result = true;
        }

        command.undoMove(this.bitBoard);
        command.undoMove(this.squareBoard);

        return result;
    }

    @Override
    public boolean isLegalMoveKing(Move move, MoveCommand command) {
        boolean result = false;
        final Color currentTurn = positionState.getCurrentTurn();

        command.doMove(this.kingCacheBoard);
        command.doMove(this.squareBoard);
        command.doMove(this.bitBoard);

        if (!fullScanSquareCapturer.isCaptured(currentTurn.oppositeColor(), kingCacheBoard.getKingSquare(currentTurn))) {
            result = true;
        }

        command.undoMove(this.bitBoard);
        command.undoMove(this.squareBoard);
        command.undoMove(this.kingCacheBoard);

        return result;
    }

    //TODO: este metodo esta consumiendo el 20% del procesamiento,
    // 		deberia crear CAPTURER especifico para validar castling
    @Override
    public boolean isLegalMoveCastling(MoveCastling moveCastling, MoveCommand command) {
        Color opositeColor = moveCastling.getFrom().getPiece().getColor().oppositeColor();
        //assert(!capturer.positionCaptured(oppositeColor, moveCastling.getFrom().getKey())); 					// El king no esta en jaque... lo asumimos
        return !fullScanSquareCapturer.isCaptured(opositeColor, moveCastling.getRookTo().getSquare())            // El king no puede ser capturado en casillero intermedio
                && !fullScanSquareCapturer.isCaptured(opositeColor, moveCastling.getTo().getSquare());            // El king no puede  ser capturado en casillero destino

    }

}
