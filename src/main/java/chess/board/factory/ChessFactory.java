package chess.board.factory;

import chess.board.Game;
import chess.board.GameState;
import chess.board.analyzer.CheckAndPinnedAnalyzer;
import chess.board.analyzer.PositionAnalyzer;
import chess.board.movesgenerators.legal.squarecapturers.FullScanSquareCapturer;
import chess.board.movesgenerators.legal.LegalMoveGenerator;
import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.movesgenerators.legal.filters.CheckMoveFilter;
import chess.board.movesgenerators.legal.filters.NoCheckMoveFilter;
import chess.board.movesgenerators.legal.imp.LegalMoveGeneratorImp;
import chess.board.movesgenerators.legal.strategies.CheckLegalMoveGenerator;
import chess.board.movesgenerators.legal.strategies.NoCheckLegalMoveGenerator;
import chess.board.position.ChessPosition;
import chess.board.position.ChessPositionReader;
import chess.board.position.PiecePlacement;
import chess.board.position.PiecePlacementReader;
import chess.board.position.imp.ArrayPiecePlacement;
import chess.board.position.imp.ChessPositionImp;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.position.imp.PositionState;
import chess.board.movesgenerators.pseudo.MoveGenerator;
import chess.board.movesgenerators.pseudo.imp.MoveGenaratorWithCacheProxy;
import chess.board.movesgenerators.pseudo.imp.MoveGeneratorImp;

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

	public Game createGame(ChessPosition chessPosition, PositionAnalyzer analyzer, GameState gameState) {
		return new Game(chessPosition, analyzer, gameState);
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
