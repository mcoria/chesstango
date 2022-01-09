package chess.builder;

import chess.ChessPosition;
import chess.BoardState;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PiecePlacement;
import chess.layers.imp.ArrayPiecePlacement;
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
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
		return new DefaultLegalMoveCalculator(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy, filter);
	}

	public LegalMoveCalculator createNoCheckLegalMoveCalculator(PiecePlacement buildPosicionPiezaBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
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

	public MoveFilter createMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, BoardState boardState, Capturer capturer) {
		return new MoveFilter(dummyBoard, kingCacheBoard, colorBoard, boardState, capturer);
	}

	public PiecePlacement createPosicionPiezaBoard() {
		return new ArrayPiecePlacement();
	}

	public BoardState createBoardState() {
		return new BoardState();
	}

}
