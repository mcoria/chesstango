package debug.builder;

import builder.ChessFactory;
import chess.Board;
import chess.BoardState;
import debug.chess.BoardDebug;
import debug.chess.BoardStateDebug;
import debug.chess.ColorBoardDebug;
import debug.chess.DefaultLegalMoveCalculatorDebug;
import debug.chess.KingCacheBoardDebug;
import debug.chess.MoveCacheBoardDebug;
import debug.chess.MoveFilterDebug;
import debug.chess.NoCheckLegalMoveCalculatorDebug;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.MoveFilter;
import movecalculators.NoCheckLegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;
import positioncaptures.Capturer;

public class DebugChessFactory extends ChessFactory {

	@Override
	public Board createBoard() {
		return  new BoardDebug();
	}	
	
	@Override
	public DefaultLegalMoveCalculator createDefaultLegalMoveCalculator(PosicionPiezaBoard buildDummyBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
		
		return new DefaultLegalMoveCalculatorDebug(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy, filter);
	}
	
	@Override
	public NoCheckLegalMoveCalculator createNoCheckLegalMoveCalculator(PosicionPiezaBoard buildPosicionPiezaBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
		return new NoCheckLegalMoveCalculatorDebug(buildPosicionPiezaBoard, buildKingCacheBoard, buildColorBoard,
				buildMoveCache, buildState, buildMoveGeneratorStrategy, filter);
	}	
	
	@Override
	public ColorBoard createColorBoard(PosicionPiezaBoard buildPosicionPiezaBoard) {
		ColorBoard colorBoard = new ColorBoardDebug(buildPosicionPiezaBoard);
		return colorBoard;
	}	
	
	@Override
	public KingCacheBoard createKingCacheBoard(PosicionPiezaBoard posicionPiezaBoard) {
		return new KingCacheBoardDebug(posicionPiezaBoard);
	}
	
	@Override
	public MoveCacheBoard createMoveCacheBoard() {
		return new MoveCacheBoardDebug();
	}
	
	@Override
	public MoveFilter createMoveFilter(PosicionPiezaBoard dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, BoardState boardState, Capturer capturer) {
		return new MoveFilterDebug(dummyBoard, kingCacheBoard, colorBoard, boardState, capturer);
	}
	
	@Override
	public BoardState createBoardState() {
		return new BoardStateDebug();
	}
}
