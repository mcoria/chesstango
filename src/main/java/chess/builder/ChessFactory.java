package chess.builder;

import chess.ChessPositionReader;
import chess.analyzer.Capturer;
import chess.legalmovesgenerators.DefaultLegalMoveGenerator;
import chess.legalmovesgenerators.LegalMoveGenerator;
import chess.legalmovesgenerators.MoveFilter;
import chess.legalmovesgenerators.NoCheckLegalMoveGenerator;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.position.imp.ArrayPiecePlacement;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.imp.MoveGenaratorWithCache;

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

	public MoveGenerator createMoveGenaratorWithCache(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
		return new MoveGenaratorWithCache(moveGenerator, moveCacheBoard);
	}

}
