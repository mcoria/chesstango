package chess.factory;

import chess.ChessPositionReader;
import chess.Game;
import chess.analyzer.Capturer;
import chess.analyzer.PositionAnalyzer;
import chess.legalmovesgenerators.LegalMoveGenerator;
import chess.legalmovesgenerators.MoveFilter;
import chess.legalmovesgenerators.strategies.DefaultLegalMoveGenerator;
import chess.legalmovesgenerators.strategies.NoCheckLegalMoveGenerator;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.position.imp.ArrayPiecePlacement;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.imp.MoveGenaratorWithCacheProxy;
import chess.pseudomovesgenerators.imp.MoveGeneratorImp;

/**
 * @author Mauricio Coria
 *
 */
public class ChessFactory {

	public ChessPosition createChessPosition() {
		return new ChessPosition();
	}

	public LegalMoveGenerator createDefaultLegalMoveGenerator(ChessPositionReader positionReader,
			MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
		return new DefaultLegalMoveGenerator(positionReader, buildMoveGeneratorStrategy, filter);
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

	public Game createGame(ChessPosition chessPosition, PositionAnalyzer analyzer) {
		return new Game(chessPosition, analyzer);
	}

	public PositionAnalyzer createPositionAnalyzer() {
		return  new PositionAnalyzer();
	}

	public Capturer creareCapturer(ChessPosition chessPosition) {
		return new Capturer(chessPosition);
	}

}
