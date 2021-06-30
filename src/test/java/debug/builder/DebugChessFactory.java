package debug.builder;

import builder.ChessFactory;
import chess.Board;
import chess.BoardState;
import debug.chess.DebugBoard;
import debug.chess.DefaultLegalMoveCalculatorDebug;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;

public class DebugChessFactory extends ChessFactory {

	@Override
	public Board createBoard() {
		return  new DebugBoard();
	}	
	
	@Override
	public DefaultLegalMoveCalculator createDefaultLegalMoveCalculator(PosicionPiezaBoard buildDummyBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy) {
		
		return new DefaultLegalMoveCalculatorDebug(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy);
	}
}
