package debug.builder;

import builder.ChessFactory;
import chess.Board;
import chess.BoardState;
import debug.chess.BoardDebug;
import debug.chess.ColorBoardDebug;
import debug.chess.DefaultLegalMoveCalculatorDebug;
import debug.chess.NoCheckLegalMoveCalculatorDebug;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;

public class DebugChessFactory extends ChessFactory {

	@Override
	public Board createBoard() {
		return  new BoardDebug();
	}	
	
	@Override
	public DefaultLegalMoveCalculator createDefaultLegalMoveCalculator(PosicionPiezaBoard buildDummyBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy) {
		
		return new DefaultLegalMoveCalculatorDebug(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy);
	}
	
	public NoCheckLegalMoveCalculator createNoCheckLegalMoveCalculator(PosicionPiezaBoard buildPosicionPiezaBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy) {
		return new NoCheckLegalMoveCalculatorDebug(buildPosicionPiezaBoard, buildKingCacheBoard, buildColorBoard,
				buildMoveCache, buildState, buildMoveGeneratorStrategy);
	}	
	
	public ColorBoard createColorBoard(PosicionPiezaBoard buildPosicionPiezaBoard, KingCacheBoard buildKingCacheBoard) {
		return new ColorBoardDebug(buildPosicionPiezaBoard, buildKingCacheBoard);
	}	
}
