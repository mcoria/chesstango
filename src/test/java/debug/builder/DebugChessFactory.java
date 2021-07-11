package debug.builder;

import builder.ChessFactory;
import chess.Board;
import chess.BoardState;
import debug.chess.BoardDebug;
import debug.chess.ColorBoardDebug;
import debug.chess.DefaultLegalMoveCalculatorDebug;
import debug.chess.KingCacheBoardDebug;
import debug.chess.MoveCacheBoardDebug;
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
	
	public ColorBoard createColorBoard(PosicionPiezaBoard buildPosicionPiezaBoard, KingCacheBoard kingCacheBoard) {
		ColorBoard colorBoard = new ColorBoardDebug(buildPosicionPiezaBoard);
		colorBoard.setKingCacheBoard(kingCacheBoard);
		return colorBoard;
	}	
	
	public KingCacheBoard createKingCacheBoard(PosicionPiezaBoard posicionPiezaBoard) {
		return new KingCacheBoardDebug(posicionPiezaBoard);
	}
	
	public MoveCacheBoard createMoveCacheBoard() {
		return new MoveCacheBoardDebug();
	}	
}
