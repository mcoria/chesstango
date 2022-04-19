package chess.board.debug.builder;

import chess.board.analyzer.capturer.Capturer;
import chess.board.debug.chess.ChessPositionDebug;
import chess.board.debug.chess.ColorBoardDebug;
import chess.board.debug.chess.DefaultLegalMoveGeneratorDebug;
import chess.board.debug.chess.KingCacheBoardDebug;
import chess.board.debug.chess.MoveCacheBoardDebug;
import chess.board.debug.chess.MoveFilterDebug;
import chess.board.debug.chess.MoveGenaratorWithCacheDebug;
import chess.board.debug.chess.NoCheckLegalMoveGeneratorDebug;
import chess.board.debug.chess.PositionStateDebug;
import chess.board.factory.ChessFactory;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.legalmovesgenerators.strategies.CheckLegalMoveGenerator;
import chess.board.legalmovesgenerators.strategies.NoCheckLegalMoveGenerator;
import chess.board.position.ChessPositionReader;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ChessPositionImp;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.position.imp.PositionState;
import chess.board.pseudomovesgenerators.MoveGenerator;
import chess.board.pseudomovesgenerators.imp.MoveGeneratorImp;


/**
 * @author Mauricio Coria
 *
 */
public class ChessFactoryDebug extends ChessFactory {

	@Override
	public ChessPositionImp createChessPosition() {
		return  new ChessPositionDebug();
	}	
	
	@Override
	public CheckLegalMoveGenerator createDefaultLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
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
	public MoveGenerator createMoveGenaratorWithCacheProxy(MoveGeneratorImp moveGenerator, MoveCacheBoard moveCacheBoard) {
		return new MoveGenaratorWithCacheDebug(moveGenerator, moveCacheBoard);
	}	
}
