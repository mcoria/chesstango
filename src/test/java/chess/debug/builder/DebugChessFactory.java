package chess.debug.builder;

import chess.builder.ChessFactory;
import chess.debug.chess.ChessPositionDebug;
import chess.debug.chess.PositionStateDebug;
import chess.debug.chess.ColorBoardDebug;
import chess.debug.chess.DefaultLegalMoveCalculatorDebug;
import chess.debug.chess.KingCacheBoardDebug;
import chess.debug.chess.MoveCacheBoardDebug;
import chess.debug.chess.MoveFilterDebug;
import chess.debug.chess.NoCheckLegalMoveCalculatorDebug;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
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
	public ChessPosition createChessPosition() {
		return  new ChessPositionDebug();
	}	
	
	@Override
	public DefaultLegalMoveCalculator createDefaultLegalMoveCalculator(PiecePlacement buildDummyBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			PositionState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
		
		return new DefaultLegalMoveCalculatorDebug(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy, filter);
	}
	
	@Override
	public NoCheckLegalMoveCalculator createNoCheckLegalMoveCalculator(PiecePlacement buildPosicionPiezaBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			PositionState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
		return new NoCheckLegalMoveCalculatorDebug(buildPosicionPiezaBoard, buildKingCacheBoard, buildColorBoard,
				buildMoveCache, buildState, buildMoveGeneratorStrategy, filter);
	}	
	
	@Override
	public ColorBoard createColorBoard(PiecePlacement buildPosicionPiezaBoard) {
		ColorBoard colorBoard = new ColorBoardDebug(buildPosicionPiezaBoard);
		return colorBoard;
	}	
	
	@Override
	public KingCacheBoard createKingCacheBoard(PiecePlacement piecePlacement) {
		return new KingCacheBoardDebug(piecePlacement);
	}
	
	@Override
	public MoveCacheBoard createMoveCacheBoard(PiecePlacement piecePlacement, MoveGeneratorStrategy moveGeneratorStrategy) {
		return new MoveCacheBoardDebug(piecePlacement, moveGeneratorStrategy);
	}
	
	@Override
	public MoveFilter createMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState, Capturer capturer) {
		return new MoveFilterDebug(dummyBoard, kingCacheBoard, colorBoard, positionState, capturer);
	}
	
	@Override
	public PositionState createPositionState() {
		return new PositionStateDebug();
	}
	
}
