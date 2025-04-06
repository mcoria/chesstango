package net.chesstango.board.internal.factory;

import net.chesstango.board.internal.position.*;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.internal.moves.generators.legal.check.CheckLegalMoveFilter;
import net.chesstango.board.internal.moves.generators.legal.check.CheckLegalMoveFilterDebug;
import net.chesstango.board.internal.moves.generators.legal.check.CheckLegalMoveGeneratorDebug;
import net.chesstango.board.internal.moves.generators.legal.nocheck.NoCheckLegalMoveFilter;
import net.chesstango.board.internal.moves.generators.legal.check.CheckLegalMoveGenerator;
import net.chesstango.board.internal.moves.generators.legal.nocheck.NoCheckLegalMoveFilterDebug;
import net.chesstango.board.internal.moves.generators.legal.nocheck.NoCheckLegalMoveGenerator;
import net.chesstango.board.internal.moves.generators.legal.nocheck.NoCheckLegalMoveGeneratorDebug;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.internal.moves.generators.pseudo.MoveGeneratorCacheDebug;
import net.chesstango.board.position.*;


/**
 * @author Mauricio Coria
 *
 */
public class ChessFactoryDebug extends ChessFactory {

	@Override
	public PositionImp createChessPosition() {
		return new PositionDebug();
	}	
	
	@Override
	public CheckLegalMoveGenerator createCheckLegalMoveGenerator(PositionReader positionReader, MoveGenerator buildMoveGeneratorStrategy, LegalMoveFilter filter) {
		return new CheckLegalMoveGeneratorDebug(positionReader, buildMoveGeneratorStrategy, filter);
	}
	
	@Override
	public NoCheckLegalMoveGenerator createNoCheckLegalMoveGenerator(PositionReader positionReader, MoveGenerator buildMoveGeneratorStrategy, LegalMoveFilter filter) {
		return new NoCheckLegalMoveGeneratorDebug(positionReader, buildMoveGeneratorStrategy, filter);
	}
	
	@Override
	public BitBoard createColorBoard() {
        return new BitBoardDebug();
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
	public CheckLegalMoveFilter createCheckMoveFilter(SquareBoard dummySquareBoard, KingSquare kingCacheBoard, BitBoard bitBoard, State state) {
		return new CheckLegalMoveFilterDebug(dummySquareBoard, kingCacheBoard, bitBoard, state);
	}

	@Override
	public NoCheckLegalMoveFilter createNoCheckMoveFilter(SquareBoard dummySquareBoard, KingSquare kingCacheBoard, BitBoard bitBoard,
                                                          State state) {
		return new NoCheckLegalMoveFilterDebug(dummySquareBoard, kingCacheBoard, bitBoard, state);
	}		
	
	@Override
	public State createPositionState() {
		return new StateDebug();
	}
	
	@Override
	public MoveGenerator createMoveGeneratorWithCacheProxy(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
		return new MoveGeneratorCacheDebug(moveGenerator, moveCacheBoard);
	}

	@Override
	public ZobristHash createZobristHash() {
		return new ZobristHashDebug();
	}
}
