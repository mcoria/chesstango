package debug.chess;

import chess.BoardState;
import chess.Move;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.PosicionPiezaBoard;
import layers.imp.ArrayPosicionPiezaBoard;
import movecalculators.MoveFilter;
import positioncaptures.Capturer;

public class MoveFilterDebug extends MoveFilter {
	
	public MoveFilterDebug(PosicionPiezaBoard dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
			BoardState boardState, Capturer capturer) {
		super(dummyBoard, kingCacheBoard, colorBoard, boardState, capturer);
	}

	@Override
	public boolean filterMove(Move move) {
		try {
			boolean reportError = false;

			ArrayPosicionPiezaBoard boardInicial = ((ArrayPosicionPiezaBoard) super.dummyBoard).clone();
			
			KingCacheBoard kingCacheBoardInicial = super.kingCacheBoard.clone();
			
			BoardState boardStateInicial = super.boardState.clone();
	
			boolean result = super.filterMove(move);
			
			if (!super.boardState.equals(boardStateInicial)) {
				System.out.println("El estado fué modificado");
				System.out.println("Inicial [" + boardStateInicial.toString() + "]\n" + "Final   [" + super.boardState.toString() + "]\n");
				reportError = true;				
			}			
			
			if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de rey fué modificado");
				System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + super.kingCacheBoard.toString() + "]\n");
				reportError = true;
			}
	
			if (!super.dummyBoard.equals(boardInicial)) {
				System.out.println("El board fué modificado");
				System.out.println("Inicial:\n" + boardInicial.toString() + "\n" + "Final:\n" + super.dummyBoard.toString());
				reportError = true;				
			}
	
			if (reportError) {
				System.out.println("El filtrado del moviemiento [" + move + "] causo la inconsistencia");
				throw new RuntimeException("Hubo modificaciones ! ! !");
			}
			
			((BoardStateDebug)boardState).validar(this.dummyBoard);
			((ColorBoardDebug)colorBoard).validar(this.dummyBoard);	
			
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean filterKingMove(Move move) {
		try {
			boolean reportError = false;	
			
			KingCacheBoard kingCacheBoardInicial = super.kingCacheBoard.clone();
	
			boolean result = super.filterKingMove(move);			
			
			if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de rey fué modificado");
				System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + super.kingCacheBoard.toString() + "]\n");
				reportError = true;
			}
	
			if (reportError) {
				System.out.println("El filtrado del moviemiento [" + move + "] causo la inconsistencia");
				throw new RuntimeException("Hubo modificaciones ! ! !");
			}
			
			((KingCacheBoardDebug)kingCacheBoard).validar(this.dummyBoard);			
			
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
