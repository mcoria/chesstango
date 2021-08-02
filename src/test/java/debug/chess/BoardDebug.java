package debug.chess;

import chess.Board;
import chess.BoardState;
import chess.BoardStatus;
import chess.Move;
import layers.KingCacheBoard;
import layers.imp.ArrayPosicionPiezaBoard;

public class BoardDebug extends Board {
	
	
	
	@Override
	public void execute(Move move) {
		super.execute(move);
		
		((BoardStateDebug)boardState).validar(this.dummyBoard);
		((ColorBoardDebug)colorBoard).validar(this.dummyBoard);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.dummyBoard);			
	}
	
	@Override
	// TODO: validadciones del metodo getBoardStatus(): 
	//      								a) no modifica el tablero
	//										b) no modifica el estado
	//										c) no modifica el cache de rey
	public BoardStatus getBoardStatus() {
		try {
			boolean reportError = false;
			
			ArrayPosicionPiezaBoard boardInicial = ((ArrayPosicionPiezaBoard) super.dummyBoard).clone();
			
			KingCacheBoard kingCacheBoardInicial = super.kingCacheBoard.clone();
			
			BoardState boardStateInicial = super.boardState.clone();

			BoardStatus result = super.getBoardStatus();
			
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
				throw new RuntimeException("Hubo modificaciones ! ! !");
			}
			
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
