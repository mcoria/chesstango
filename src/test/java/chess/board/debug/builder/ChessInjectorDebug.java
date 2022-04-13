/**
 * 
 */
package chess.board.debug.builder;

import chess.board.debug.chess.ChessPositionDebug;
import chess.board.factory.ChessInjector;
import chess.board.position.ChessPosition;

/**
 * @author Mauricio Coria
 *
 */
public class ChessInjectorDebug extends ChessInjector {
	

	public ChessInjectorDebug() {
		super(new ChessFactoryDebug());
	}


	@Override
	public ChessPosition getChessPosition() {
		ChessPositionDebug chessPositionDebug = (ChessPositionDebug) super.getChessPosition();
		
		chessPositionDebug.setMoveGeneratorImp(this.getMoveGeneratorImp());
		
		return chessPositionDebug;
	}
	
}
