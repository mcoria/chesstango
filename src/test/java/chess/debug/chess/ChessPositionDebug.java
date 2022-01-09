package chess.debug.chess;

import chess.BoardStatus;
import chess.moves.Move;
import chess.position.ChessPosition;
import chess.position.KingCacheBoard;
import chess.position.PositionState;
import chess.position.imp.ArrayPiecePlacement;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionDebug extends ChessPosition {
	

	@Override
	public void execute(Move move) {
		super.execute(move);
		((PositionStateDebug)positionState).validar(this.dummyBoard);
		((ColorBoardDebug)colorBoard).validar(this.dummyBoard);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.dummyBoard);
		((MoveCacheBoardDebug)moveCache).validar(this.dummyBoard);
	}

	@Override
	public void undo(Move move) {
		super.undo(move);
		((PositionStateDebug)positionState).validar(this.dummyBoard);
		((ColorBoardDebug)colorBoard).validar(this.dummyBoard);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.dummyBoard);
		((MoveCacheBoardDebug)moveCache).validar(this.dummyBoard);		
	}
	
	@Override
	// TODO: validadciones del metodo getBoardStatus(): 
	//      								a) no modifica el tablero
	//										b) no modifica el estado
	//										c) no modifica el cache de king
	public BoardStatus getBoardStatus() {
		try {
			boolean reportError = false;
			
			ArrayPiecePlacement boardInicial = ((ArrayPiecePlacement) super.dummyBoard).clone();
			
			KingCacheBoard kingCacheBoardInicial = super.kingCacheBoard.clone();
			
			PositionState boardStateInicial = super.positionState.clone();

			BoardStatus result = super.getBoardStatus();
			
			if (!super.positionState.equals(boardStateInicial)) {
				System.out.println("El estado fué modificado");
				System.out.println("Inicial [" + boardStateInicial.toString() + "]\n" + "Final   [" + super.positionState.toString() + "]\n");
				reportError = true;				
			}			
			
			if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de king fué modificado");
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
