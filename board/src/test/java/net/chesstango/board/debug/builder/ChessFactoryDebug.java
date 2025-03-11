package net.chesstango.board.debug.builder;

import net.chesstango.board.debug.chess.*;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.strategies.check.CheckLegalMoveFilter;
import net.chesstango.board.moves.generators.legal.strategies.nocheck.NoCheckLegalMoveFilter;
import net.chesstango.board.moves.generators.legal.strategies.check.CheckLegalMoveGenerator;
import net.chesstango.board.moves.generators.legal.strategies.nocheck.NoCheckLegalMoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
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
	public CheckLegalMoveGenerator createCheckLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator buildMoveGeneratorStrategy, LegalMoveFilter filter) {
		return new DefaultLegalMoveGeneratorDebug(positionReader, buildMoveGeneratorStrategy, filter);
	}
	
	@Override
	public NoCheckLegalMoveGenerator createNoCheckLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator buildMoveGeneratorStrategy, LegalMoveFilter filter) {
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
	public CheckLegalMoveFilter createCheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard, PositionState positionState) {
		return new CheckLegalMoveFilterDebug(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
	}

	@Override
	public NoCheckLegalMoveFilter createNoCheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard,
                                                          PositionState positionState) {
		return new NoCheckLegalMoveFilterDebug(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
	}		
	
	@Override
	public PositionState createPositionState() {
		return new PositionStateDebug();
	}
	
	@Override
	public MoveGenerator createMoveGeneratorWithCacheProxy(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
		return new MoveGenaratorCacheDebug(moveGenerator, moveCacheBoard);
	}

	@Override
	public ZobristHash createZobristHash() {
		return new ZobristHashDebug();
	}
}
