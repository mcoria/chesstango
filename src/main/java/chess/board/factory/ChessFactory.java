package chess.board.factory;

import chess.board.Game;
import chess.board.GameState;
import chess.board.analyzer.Capturer;
import chess.board.analyzer.PositionAnalyzer;
import chess.board.legalmovesgenerators.LegalMoveGenerator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.legalmovesgenerators.imp.LegalMoveGeneratorImp;
import chess.board.legalmovesgenerators.strategies.CheckLegalMoveGenerator;
import chess.board.legalmovesgenerators.strategies.NoCheckLegalMoveGenerator;
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
import chess.board.pseudomovesgenerators.MoveGenerator;
import chess.board.pseudomovesgenerators.imp.MoveGenaratorWithCacheProxy;
import chess.board.pseudomovesgenerators.imp.MoveGeneratorImp;

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
		ColorBoard colorBoard = new ColorBoard();
		return colorBoard;
	}

	public KingCacheBoard createKingCacheBoard() {
		return new KingCacheBoard();
	}

	public MoveCacheBoard createMoveCacheBoard() {
		return new MoveCacheBoard();
	}

	public MoveFilter createMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
			PositionState positionState, Capturer capturer) {
		return new MoveFilter(dummyBoard, kingCacheBoard, colorBoard, positionState, capturer);
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

	public Capturer creareCapturer(PiecePlacementReader piecePlacementReader) {
		return new Capturer(piecePlacementReader);
	}


	public GameState createGameState() {
		return new GameState();
	}

}
