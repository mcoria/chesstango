package net.chesstango.board.internal.moves.generators.legal.check;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.internal.moves.generators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.position.*;

/**
 * Este filtro se utiliza cuando el jugador actual SI se encuentra en jaque
 *
 * @author Mauricio Coria
 */
public class CheckLegalMoveFilter implements LegalMoveFilter {

    protected final SquareBoard squareBoard;
    protected final KingSquare kingCacheBoard;
    protected final BitBoard bitBoard;
    protected final StateReader positionState;
    protected final FullScanSquareCaptured fullScanSquareCapturer;

    public CheckLegalMoveFilter(SquareBoard squareBoard, KingSquare kingCacheBoard, BitBoard bitBoard, StateReader positionState) {
        this.squareBoard = squareBoard;
        this.kingCacheBoard = kingCacheBoard;
        this.bitBoard = bitBoard;
        this.positionState = positionState;
        this.fullScanSquareCapturer = new FullScanSquareCaptured(squareBoard, bitBoard);
    }

    @Override
    public boolean isLegalMovePawn(Move move, Command command) {
        return isLegalMove(move, command);
    }

    @Override
    public boolean isLegalMoveKnight(Move move, Command command) {
        return isLegalMove(move, command);
    }

    @Override
    public boolean isLegalMoveBishop(Move move, Command command) {
        return isLegalMove(move, command);
    }

    @Override
    public boolean isLegalMoveRook(Move move, Command command) {
        return isLegalMove(move, command);
    }

    @Override
    public boolean isLegalMoveQueen(Move move, Command command) {
        return isLegalMove(move, command);
    }

    @Override
    public boolean isLegalMoveKing(Move move, Command command) {
        command.doMove(this.kingCacheBoard);

        boolean result = isLegalMove(move, command);

        command.undoMove(this.kingCacheBoard);

        return result;
    }

    @Override
    public boolean isLegalMoveCastling(MoveCastling move, Command command) {
        return false;
    }


    protected boolean isLegalMove(Move move, Command command) {
        boolean result = false;

        final Color currentTurn = positionState.getCurrentTurn();

        command.doMove(this.squareBoard);
        command.doMove(this.bitBoard);

        if (!fullScanSquareCapturer.isCaptured(currentTurn.oppositeColor(), kingCacheBoard.getKingSquare(currentTurn))) {
            result = true;
        }

        command.undoMove(this.bitBoard);
        command.undoMove(this.squareBoard);

        return result;
    }

}
