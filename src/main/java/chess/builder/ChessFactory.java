package chess.builder;

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

/**
 * @author Mauricio Coria
 *
 */
public class ChessFactory {

	public ChessPosition createChessPosition() {
		return new ChessPosition();
	}

	public LegalMoveGenerator createDefaultLegalMoveGenerator(PiecePlacement buildDummyBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			PositionState buildState, MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
		return new DefaultLegalMoveGenerator(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy, filter);
	}

	public LegalMoveGenerator createNoCheckLegalMoveGenerator(PiecePlacement buildPosicionPiezaBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			PositionState buildState, MoveGenerator buildMoveGeneratorStrategy, MoveFilter filter) {
		return new NoCheckLegalMoveGenerator(buildPosicionPiezaBoard, buildKingCacheBoard, buildColorBoard,
				buildMoveCache, buildState, buildMoveGeneratorStrategy, filter);
	}

	public ColorBoard createColorBoard() {
		ColorBoard colorBoard = new ColorBoard();
		return colorBoard;
	}

	public KingCacheBoard createKingCacheBoard(PiecePlacement piecePlacement) {
		return new KingCacheBoard(piecePlacement);
	}

	public MoveCacheBoard createMoveCacheBoard() {
		return new MoveCacheBoard();
	}

	public MoveFilter createMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState, Capturer capturer) {
		return new MoveFilter(dummyBoard, kingCacheBoard, colorBoard, positionState, capturer);
	}

	public PiecePlacement createPiecePlacement() {
		return new ArrayPiecePlacement();
	}

	public PositionState createPositionState() {
		return new PositionState();
	}

}
