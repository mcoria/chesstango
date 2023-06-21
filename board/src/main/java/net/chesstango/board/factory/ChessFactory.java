package net.chesstango.board.factory;

import net.chesstango.board.Game;
import net.chesstango.board.GameImp;
import net.chesstango.board.GameState;
import net.chesstango.board.analyzer.CheckAnalyzer;
import net.chesstango.board.analyzer.PinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.filters.CheckMoveFilter;
import net.chesstango.board.movesgenerators.legal.filters.NoCheckMoveFilter;
import net.chesstango.board.movesgenerators.legal.imp.LegalMoveGeneratorImp;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.movesgenerators.legal.strategies.CheckLegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.strategies.NoCheckLegalMoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorImp;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorWithCacheProxy;
import net.chesstango.board.position.*;
import net.chesstango.board.position.imp.*;

import java.util.Map;

/**
 * @author Mauricio Coria
 *
 */
public class ChessFactory {

	public ChessPositionImp createChessPosition() {
		return new ChessPositionImp();
	}

	public LegalMoveGeneratorImp createLegalMoveGenerator() {
		return new LegalMoveGeneratorImp();
	}
	
	public LegalMoveGenerator createCheckLegalMoveGenerator(ChessPositionReader positionReader,
															MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
		return new CheckLegalMoveGenerator(positionReader, buildMoveGeneratorStrategy, filter);
	}

	public LegalMoveGenerator createNoCheckLegalMoveGenerator(ChessPositionReader positionReader,
			MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
		return new NoCheckLegalMoveGenerator(positionReader, buildMoveGeneratorStrategy, filter);
	}

	public BitBoard createColorBoard() {
		return new BitBoardImp();
	}

	public KingSquareImp createKingCacheBoard() {
		return new KingSquareImp();
	}

	public MoveCacheBoardImp createMoveCacheBoard() {
		return new MoveCacheBoardImp();
	}

	public CheckMoveFilter createCheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard,
												 PositionState positionState) {
		return new CheckMoveFilter(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
	}
	
	public NoCheckMoveFilter createNoCheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard,
													 PositionState positionState) {
		return new NoCheckMoveFilter(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
	}	

	public SquareBoard createPiecePlacement() {
		return new SquareBoardImp();
	}

	public PositionState createPositionState() {
		return new PositionStateImp();
	}

	public MoveGenerator createMoveGeneratorWithCacheProxy(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
		return new MoveGeneratorWithCacheProxy(moveGenerator, moveCacheBoard);
	}

	public MoveGeneratorImp createMoveGenerator() {
		return new MoveGeneratorImp();
	}

	public Game createGame(ChessPosition chessPosition, GameState gameState, PositionAnalyzer analyzer, Map<Class, Object> objectMap) {
		return new GameImp(chessPosition, gameState, analyzer, objectMap);
	}

	public PositionAnalyzer createPositionAnalyzer() {
		return  new PositionAnalyzer();
	}

	public FullScanSquareCaptured createCapturer(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader) {
		return new FullScanSquareCaptured(squareBoardReader, bitBoardReader);
	}


	public GameState createGameState() {
		return new GameState();
	}


	public CheckAnalyzer createCheckAnalyzer(ChessPositionReader positionReader, MoveCacheBoard moveCacheBoard) {
		return new CheckAnalyzer(positionReader, moveCacheBoard);
	}

	public PinnedAnalyzer createPinnedAnalyzer(ChessPosition chessPosition) {
		return new PinnedAnalyzer(chessPosition);
	}

    public ZobristHash createZobristHash() {
		return new ZobristHashImp();
    }

}
