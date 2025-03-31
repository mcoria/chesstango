package net.chesstango.board.debug.chess;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.generators.legal.imp.nocheck.NoCheckLegalMoveFilter;
import net.chesstango.board.position.*;
import net.chesstango.board.position.imp.KingSquareImp;
import net.chesstango.board.position.imp.PositionStateImp;
import net.chesstango.board.position.imp.SquareBoardImp;


/**
 * @author Mauricio Coria
 */
public class NoCheckLegalMoveFilterDebug extends NoCheckLegalMoveFilter {

    public NoCheckLegalMoveFilterDebug(SquareBoard dummySquareBoard, KingSquare kingCacheBoard, BitBoard bitBoard,
                                       PositionState positionState) {
        super(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
    }

    @Override
    public boolean isLegalMove(Move move, Command command) {
        try {
            boolean reportError = false;

            SquareBoardImp boardInicial = ((SquareBoardImp) super.squareBoard).clone();

            KingSquareImp kingCacheBoardInicial = ((KingSquareImp) super.kingCacheBoard).clone();

            PositionStateImp boardStateInicial = ((PositionStateImp) positionState).clone();

            boolean result = super.isLegalMove(move, command);

            if (!super.positionState.equals(boardStateInicial)) {
                System.out.println("El estado fu� modificado");
                System.out.println("Inicial [" + boardStateInicial.toString() + "]\n" + "Final   [" + super.positionState + "]\n");
                reportError = true;
            }

            if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
                System.out.println("El cache de king fu� modificado");
                System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + super.kingCacheBoard + "]\n");
                reportError = true;
            }

            if (!super.squareBoard.equals(boardInicial)) {
                System.out.println("El board fu� modificado");
                System.out.println("Inicial:\n" + boardInicial.toString() + "\n" + "Final:\n" + super.squareBoard);
                reportError = true;
            }

            if (reportError) {
                System.out.println("El filtrado del moviemiento [" + move + "] causo la inconsistencia");
                throw new RuntimeException("Hubo modificaciones ! ! !");
            }

            ((PositionStateDebug) positionState).validar(this.squareBoard);
            ((BitBoardDebug) bitBoard).validar(this.squareBoard);

            return result;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isLegalMoveKing(Move move, Command command) {
        try {
            boolean reportError = false;

            KingSquareImp kingCacheBoardInicial = ((KingSquareImp)super.kingCacheBoard).clone();

            boolean result = super.isLegalMoveKing(move, command);

            if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
                System.out.println("El cache de king fu� modificado");
                System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + super.kingCacheBoard + "]\n");
                reportError = true;
            }

            if (reportError) {
                System.out.println("El filtrado del moviemiento [" + move + "] causo la inconsistencia");
                throw new RuntimeException("Hubo modificaciones ! ! !");
            }

            ((KingSquareDebug) kingCacheBoard).validar(this.squareBoard);

            return result;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
