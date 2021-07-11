package builder;

import chess.Board;
import chess.BoardState;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;

public class ChessFactory {

	public Board createBoard() {
		return new Board();
	}

	public LegalMoveCalculator createDefaultLegalMoveCalculator(PosicionPiezaBoard buildDummyBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy) {
		return new DefaultLegalMoveCalculator(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy);
	}

	public LegalMoveCalculator createNoCheckLegalMoveCalculator(PosicionPiezaBoard buildPosicionPiezaBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy) {
		return new NoCheckLegalMoveCalculator(buildPosicionPiezaBoard, buildKingCacheBoard, buildColorBoard,
				buildMoveCache, buildState, buildMoveGeneratorStrategy);
	}

	public ColorBoard createColorBoard(PosicionPiezaBoard buildPosicionPiezaBoard) {
		ColorBoard colorBoard = new ColorBoard(buildPosicionPiezaBoard);
		return colorBoard;
	}

	public KingCacheBoard createKingCacheBoard(PosicionPiezaBoard posicionPiezaBoard) {
		return new KingCacheBoard(posicionPiezaBoard);
	}

	public MoveCacheBoard createMoveCacheBoard() {
		return new MoveCacheBoard();
	}

}
