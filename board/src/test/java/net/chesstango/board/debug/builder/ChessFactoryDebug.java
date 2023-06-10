package net.chesstango.board.debug.builder;

import net.chesstango.board.debug.chess.*;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.filters.CheckMoveFilter;
import net.chesstango.board.movesgenerators.legal.filters.NoCheckMoveFilter;
import net.chesstango.board.movesgenerators.legal.strategies.CheckLegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.strategies.NoCheckLegalMoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.ColorBoard;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.imp.*;


/**
 * @author Mauricio Coria
 *
 */
public class ChessFactoryDebug extends ChessFactory {

	@Override
	public ChessPositionImp createChessPosition() {
		return new ChessPositionDebug();
	}	
	
	@Override
	public CheckLegalMoveGenerator createCheckLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
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
	public CheckMoveFilter createCheckMoveFilter(Board dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState) {
		return new CheckMoveFilterDebug(dummyBoard, kingCacheBoard, colorBoard, positionState);
	}

	@Override
	public NoCheckMoveFilter createNoCheckMoveFilter(Board dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
                                                     PositionState positionState) {
		return new NoCheckMoveFilterDebug(dummyBoard, kingCacheBoard, colorBoard, positionState);
	}		
	
	@Override
	public PositionState createPositionState() {
		return new PositionStateDebug();
	}
	
	@Override
	public MoveGenerator createMoveGeneratorWithCacheProxy(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
		return new MoveGenaratorWithCacheDebug(moveGenerator, moveCacheBoard);
	}

	@Override
	public ZobristHash createZobristHash() {
		return new ZobristHashDebug();
	}
}
