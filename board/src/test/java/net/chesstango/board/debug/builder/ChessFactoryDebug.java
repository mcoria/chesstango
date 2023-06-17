package net.chesstango.board.debug.builder;

import net.chesstango.board.debug.chess.*;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.filters.CheckMoveFilter;
import net.chesstango.board.movesgenerators.legal.filters.NoCheckMoveFilter;
import net.chesstango.board.movesgenerators.legal.strategies.CheckLegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.strategies.NoCheckLegalMoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.position.*;
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
	public BitBoard createColorBoard() {
		BitBoard bitBoard = new BitBoardDebug();
		return bitBoard;
	}	
	
	@Override
	public KingSquareImp createKingCacheBoard() {
		return new KingSquareDebug();
	}
	
	@Override
	public MoveCacheBoardImp createMoveCacheBoard() {
		return new MoveCacheBoardDebug();
	}
	
	@Override
	public CheckMoveFilter createCheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard, PositionState positionState) {
		return new CheckMoveFilterDebug(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
	}

	@Override
	public NoCheckMoveFilter createNoCheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard,
                                                     PositionState positionState) {
		return new NoCheckMoveFilterDebug(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
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
