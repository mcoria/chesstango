package chess.debug.chess;

import java.util.Collection;

import chess.layers.ChessPositionState;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PiecePlacement;
import chess.layers.imp.ArrayPiecePlacement;
import chess.moves.Move;
import chess.pseudomovesfilters.DefaultLegalMoveCalculator;
import chess.pseudomovesfilters.MoveFilter;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;


/**
 * @author Mauricio Coria
 *
 */
public class DefaultLegalMoveCalculatorDebug extends DefaultLegalMoveCalculator {

	public DefaultLegalMoveCalculatorDebug(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard,
			ColorBoard colorBoard, MoveCacheBoard moveCache, ChessPositionState chessPositionState, MoveGeneratorStrategy strategy, MoveFilter filter) {
		super(dummyBoard, kingCacheBoard, colorBoard, moveCache, chessPositionState, strategy, filter);
	}
	
	@Override
	public Collection<Move> getLegalMoves() {
		try {
			boolean reportError = false;
			
			ArrayPiecePlacement boardInicial = ((ArrayPiecePlacement) super.dummyBoard).clone();
			
			KingCacheBoard kingCacheBoardInicial = super.kingCacheBoard.clone();
			
			ChessPositionState boardStateInicial = super.chessPositionState.clone();
	
			Collection<Move> result = super.getLegalMoves();
			
			if (!super.chessPositionState.equals(boardStateInicial)) {
				System.out.println("El estado fu� modificado");
				System.out.println("Inicial [" + boardStateInicial.toString() + "]\n" + "Final   [" + super.chessPositionState.toString() + "]\n");
				reportError = true;				
			}			
			
			if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de king fu� modificado");
				System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + super.kingCacheBoard.toString() + "]\n");
				reportError = true;
			}
	
			if (!super.dummyBoard.equals(boardInicial)) {
				System.out.println("El board fu� modificado");
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
