package debug.builder;

import builder.ChessFactory;
import chess.Board;
import debug.chess.DebugBoard;

public class DebugChessFactory extends ChessFactory {

	
	public Board buildBoard() {
		return  new DebugBoard();
	}	
}
