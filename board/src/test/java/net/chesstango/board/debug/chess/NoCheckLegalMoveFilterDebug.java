package net.chesstango.board.debug.chess;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.moves.generators.legal.legalmovefilters.NoCheckLegalMoveFilter;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.imp.SquareBoardImp;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.imp.KingSquareImp;
import net.chesstango.board.position.imp.PositionStateImp;


/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveFilterDebug extends NoCheckLegalMoveFilter {
	
	public NoCheckLegalMoveFilterDebug(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard,
									   PositionState positionState) {
		super(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
	}

	@Override
	public boolean isLegalMove(Move move) {
		try {
			boolean reportError = false;

			SquareBoardImp boardInicial = ((SquareBoardImp) super.dummySquareBoard).clone();
			
			KingSquareImp kingCacheBoardInicial = super.kingCacheBoard.clone();

			PositionStateImp boardStateInicial = ((PositionStateImp)positionState).clone();
	
			boolean result = super.isLegalMove(move);
			
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
	
			if (!super.dummySquareBoard.equals(boardInicial)) {
				System.out.println("El board fu� modificado");
				System.out.println("Inicial:\n" + boardInicial.toString() + "\n" + "Final:\n" + super.dummySquareBoard);
				reportError = true;				
			}
	
			if (reportError) {
				System.out.println("El filtrado del moviemiento [" + move + "] causo la inconsistencia");
				throw new RuntimeException("Hubo modificaciones ! ! !");
			}
			
			((PositionStateDebug)positionState).validar(this.dummySquareBoard);
			((BitBoardDebug) bitBoard).validar(this.dummySquareBoard);
			
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean isLegalMove(MoveKing move) {
		try {
			boolean reportError = false;	
			
			KingSquareImp kingCacheBoardInicial = super.kingCacheBoard.clone();
	
			boolean result = super.isLegalMove(move);
			
			if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de king fu� modificado");
				System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + super.kingCacheBoard + "]\n");
				reportError = true;
			}
	
			if (reportError) {
				System.out.println("El filtrado del moviemiento [" + move + "] causo la inconsistencia");
				throw new RuntimeException("Hubo modificaciones ! ! !");
			}
			
			((KingSquareDebug)kingCacheBoard).validar(this.dummySquareBoard);
			
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
