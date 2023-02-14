package net.chesstango.board.factory;

import net.chesstango.board.Game;
import net.chesstango.board.GameState;
import net.chesstango.board.analyzer.CheckAndPinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCapturer;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.filters.CheckMoveFilter;
import net.chesstango.board.movesgenerators.legal.filters.NoCheckMoveFilter;
import net.chesstango.board.movesgenerators.legal.imp.LegalMoveGeneratorImp;
import net.chesstango.board.movesgenerators.legal.strategies.CheckLegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.strategies.NoCheckLegalMoveGenerator;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ChessPositionImp;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.KingCacheBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGenaratorWithCacheProxy;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorImp;

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
	
	public LegalMoveGenerator createDefaultLegalMoveGenerator(ChessPositionReader positionReader,
                                                              MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
		return new CheckLegalMoveGenerator(positionReader, buildMoveGeneratorStrategy, filter);
	}

	public LegalMoveGenerator createNoCheckLegalMoveGenerator(ChessPositionReader positionReader,
			MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
		return new NoCheckLegalMoveGenerator(positionReader, buildMoveGeneratorStrategy, filter);
	}

	public ColorBoard createColorBoard() {
		return new ColorBoard();
	}

	public KingCacheBoard createKingCacheBoard() {
		return new KingCacheBoard();
	}

	public MoveCacheBoard createMoveCacheBoard() {
		return new MoveCacheBoard();
	}

	public CheckMoveFilter createCheckMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
                                                 PositionState positionState) {
		return new CheckMoveFilter(dummyBoard, kingCacheBoard, colorBoard, positionState);
	}
	
	public NoCheckMoveFilter createNoCheckMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
                                                     PositionState positionState) {
		return new NoCheckMoveFilter(dummyBoard, kingCacheBoard, colorBoard, positionState);
	}	

	public PiecePlacement createPiecePlacement() {
		return new ArrayPiecePlacement();
	}

	public PositionState createPositionState() {
		return new PositionState();
	}

	public MoveGenerator createMoveGenaratorWithCacheProxy(MoveGeneratorImp moveGenerator, MoveCacheBoard moveCacheBoard) {
		return new MoveGenaratorWithCacheProxy(moveGenerator, moveCacheBoard);
	}

	public Game createGame(ChessPosition chessPosition, MoveGenerator pseudoMovesGenerator, PositionAnalyzer analyzer, GameState gameState) {
		return new Game(chessPosition, pseudoMovesGenerator, analyzer, gameState);
	}

	public PositionAnalyzer createPositionAnalyzer() {
		return  new PositionAnalyzer();
	}

	public FullScanSquareCapturer createCapturer(PiecePlacementReader piecePlacementReader) {
		return new FullScanSquareCapturer(piecePlacementReader);
	}


	public GameState createGameState() {
		return new GameState();
	}


	public CheckAndPinnedAnalyzer createCheckAndPinnedAnalyzer(ChessPositionReader positionReader) {
		return new CheckAndPinnedAnalyzer(positionReader);
	}

}