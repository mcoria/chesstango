package chess.builder;

import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.position.imp.ArrayPiecePlacement;
import chess.positioncaptures.Capturer;
import chess.pseudomovesfilters.DefaultLegalMoveCalculator;
import chess.pseudomovesfilters.LegalMoveCalculator;
import chess.pseudomovesfilters.MoveFilter;
import chess.pseudomovesfilters.NoCheckLegalMoveCalculator;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public class ChessFactory {

	public ChessPosition createBoard() {
		return new ChessPosition();
	}

	public LegalMoveCalculator createDefaultLegalMoveCalculator(PiecePlacement buildDummyBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			PositionState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
		return new DefaultLegalMoveCalculator(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy, filter);
	}

	public LegalMoveCalculator createNoCheckLegalMoveCalculator(PiecePlacement buildPosicionPiezaBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			PositionState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
		return new NoCheckLegalMoveCalculator(buildPosicionPiezaBoard, buildKingCacheBoard, buildColorBoard,
				buildMoveCache, buildState, buildMoveGeneratorStrategy, filter);
	}

	public ColorBoard createColorBoard(PiecePlacement buildPosicionPiezaBoard) {
		ColorBoard colorBoard = new ColorBoard(buildPosicionPiezaBoard);
		return colorBoard;
	}

	public KingCacheBoard createKingCacheBoard(PiecePlacement piecePlacement) {
		return new KingCacheBoard(piecePlacement);
	}

	public MoveCacheBoard createMoveCacheBoard(PiecePlacement piecePlacement, MoveGeneratorStrategy buildMoveGeneratorStrategy) {
		return new MoveCacheBoard(piecePlacement, buildMoveGeneratorStrategy);
	}

	public MoveFilter createMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState, Capturer capturer) {
		return new MoveFilter(dummyBoard, kingCacheBoard, colorBoard, positionState, capturer);
	}

	public PiecePlacement createPosicionPiezaBoard() {
		return new ArrayPiecePlacement();
	}

	public PositionState createBoardState() {
		return new PositionState();
	}

}
