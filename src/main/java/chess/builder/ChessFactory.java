package chess.builder;

import chess.Board;
import chess.BoardState;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
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

	public Board createBoard() {
		return new Board();
	}

	public LegalMoveCalculator createDefaultLegalMoveCalculator(PosicionPiezaBoard buildDummyBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
		return new DefaultLegalMoveCalculator(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy, filter);
	}

	public LegalMoveCalculator createNoCheckLegalMoveCalculator(PosicionPiezaBoard buildPosicionPiezaBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy, MoveFilter filter) {
		return new NoCheckLegalMoveCalculator(buildPosicionPiezaBoard, buildKingCacheBoard, buildColorBoard,
				buildMoveCache, buildState, buildMoveGeneratorStrategy, filter);
	}

	public ColorBoard createColorBoard(PosicionPiezaBoard buildPosicionPiezaBoard) {
		ColorBoard colorBoard = new ColorBoard(buildPosicionPiezaBoard);
		return colorBoard;
	}

	public KingCacheBoard createKingCacheBoard(PosicionPiezaBoard posicionPiezaBoard) {
		return new KingCacheBoard(posicionPiezaBoard);
	}

	public MoveCacheBoard createMoveCacheBoard(PosicionPiezaBoard posicionPiezaBoard, MoveGeneratorStrategy buildMoveGeneratorStrategy) {
		return new MoveCacheBoard(posicionPiezaBoard, buildMoveGeneratorStrategy);
	}

	public MoveFilter createMoveFilter(PosicionPiezaBoard dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, BoardState boardState, Capturer capturer) {
		return new MoveFilter(dummyBoard, kingCacheBoard, colorBoard, boardState, capturer);
	}

	public PosicionPiezaBoard createPosicionPiezaBoard() {
		return new ArrayPosicionPiezaBoard();
	}

	public BoardState createBoardState() {
		return new BoardState();
	}

}
