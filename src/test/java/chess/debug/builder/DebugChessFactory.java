package chess.debug.builder;

import chess.Board;
import chess.BoardState;
import chess.builder.ChessFactory;
import chess.debug.chess.BoardDebug;
import chess.debug.chess.BoardStateDebug;
import chess.debug.chess.ColorBoardDebug;
import chess.debug.chess.DefaultLegalMoveCalculatorDebug;
import chess.debug.chess.KingCacheBoardDebug;
import chess.debug.chess.MoveCacheBoardDebug;
import chess.debug.chess.MoveFilterDebug;
import chess.debug.chess.NoCheckLegalMoveCalculatorDebug;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.positioncaptures.Capturer;
import chess.pseudomovesfilters.DefaultLegalMoveCalculator;
import chess.pseudomovesfilters.MoveFilter;
import chess.pseudomovesfilters.NoCheckLegalMoveCalculator;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;


/**
 * @author Mauricio Coria
 *
 */
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
	public MoveCacheBoard createMoveCacheBoard(PosicionPiezaBoard posicionPiezaBoard, MoveGeneratorStrategy moveGeneratorStrategy) {
		return new MoveCacheBoardDebug(posicionPiezaBoard, moveGeneratorStrategy);
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
