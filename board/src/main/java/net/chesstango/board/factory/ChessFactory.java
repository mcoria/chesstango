package net.chesstango.board.factory;

import net.chesstango.board.*;
import net.chesstango.board.analyzer.KingSafePositionsAnalyzer;
import net.chesstango.board.analyzer.PinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.imp.check.CheckLegalMoveFilter;
import net.chesstango.board.moves.generators.legal.imp.nocheck.NoCheckLegalMoveFilter;
import net.chesstango.board.moves.generators.legal.imp.LegalMoveGeneratorImp;
import net.chesstango.board.moves.generators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.moves.generators.legal.imp.check.CheckLegalMoveGenerator;
import net.chesstango.board.moves.generators.legal.imp.nocheck.NoCheckLegalMoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.imp.MoveGeneratorImp;
import net.chesstango.board.moves.generators.pseudo.imp.MoveGeneratorCache;
import net.chesstango.board.position.*;
import net.chesstango.board.position.imp.*;

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
															MoveGenerator buildMoveGeneratorStrategy, LegalMoveFilter filter) {
		return new CheckLegalMoveGenerator(positionReader, buildMoveGeneratorStrategy, filter);
	}

	public LegalMoveGenerator createNoCheckLegalMoveGenerator(ChessPositionReader positionReader,
			MoveGenerator buildMoveGeneratorStrategy, LegalMoveFilter filter) {
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

	public CheckLegalMoveFilter createCheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard,
                                                      PositionState positionState) {
		return new CheckLegalMoveFilter(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
	}
	
	public NoCheckLegalMoveFilter createNoCheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard,
                                                          PositionState positionState) {
		return new NoCheckLegalMoveFilter(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
	}	

	public SquareBoard createPiecePlacement() {
		return new SquareBoardImp();
	}

	public PositionState createPositionState() {
		return new PositionStateImp();
	}

	public MoveGenerator createMoveGeneratorWithCacheProxy(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
		return new MoveGeneratorCache(moveGenerator, moveCacheBoard);
	}

	public MoveGeneratorImp createMoveGenerator() {
		return new MoveGeneratorImp();
	}

	public Game createGame(ChessPosition chessPosition, GameState gameState, PositionAnalyzer analyzer, GameVisitorAcceptor visitorAcceptor) {
		return new GameImp(chessPosition, gameState, analyzer, visitorAcceptor);
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

	public PinnedAnalyzer createPinnedAnalyzer(ChessPosition chessPosition) {
		return new PinnedAnalyzer(chessPosition);
	}

	public KingSafePositionsAnalyzer createKingSafePositionsAnalyzer(ChessPositionReader positionReader) {
		return new KingSafePositionsAnalyzer(positionReader);
	}

	public ZobristHash createZobristHash() {
		return new ZobristHashImp();
	}
}
