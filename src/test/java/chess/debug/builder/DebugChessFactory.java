package chess.debug.builder;

import chess.ChessPositionReader;
import chess.analyzer.Capturer;
import chess.builder.ChessFactory;
import chess.debug.chess.ChessPositionDebug;
import chess.debug.chess.ColorBoardDebug;
import chess.debug.chess.DefaultLegalMoveGeneratorDebug;
import chess.debug.chess.KingCacheBoardDebug;
import chess.debug.chess.MoveCacheBoardDebug;
import chess.debug.chess.MoveFilterDebug;
import chess.debug.chess.MoveGenaratorWithCacheDebug;
import chess.debug.chess.NoCheckLegalMoveGeneratorDebug;
import chess.debug.chess.PositionStateDebug;
import chess.legalmovesgenerators.DefaultLegalMoveGenerator;
import chess.legalmovesgenerators.MoveFilter;
import chess.legalmovesgenerators.NoCheckLegalMoveGenerator;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.pseudomovesgenerators.MoveGenerator;


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
	public DefaultLegalMoveGenerator createDefaultLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
		
		return new DefaultLegalMoveGeneratorDebug(positionReader, buildMoveGeneratorStrategy, filter);
	}
	
	@Override
	public NoCheckLegalMoveGenerator createNoCheckLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
		return new NoCheckLegalMoveGeneratorDebug(positionReader, buildMoveGeneratorStrategy, filter);
	}	
	
	@Override
	public ColorBoard createColorBoard() {
		ColorBoard colorBoard = new ColorBoardDebug();
		return colorBoard;
	}	
	
	@Override
	public KingCacheBoard createKingCacheBoard() {
		return new KingCacheBoardDebug();
	}
	
	@Override
	public MoveCacheBoard createMoveCacheBoard() {
		return new MoveCacheBoardDebug();
	}
	
	@Override
	public MoveFilter createMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState, Capturer capturer) {
		return new MoveFilterDebug(dummyBoard, kingCacheBoard, colorBoard, positionState, capturer);
	}
	
	@Override
	public PositionState createPositionState() {
		return new PositionStateDebug();
	}
	
	@Override
	public MoveGenerator createMoveGenaratorWithCache(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
		return new MoveGenaratorWithCacheDebug(moveGenerator, moveCacheBoard);
	}	
}
