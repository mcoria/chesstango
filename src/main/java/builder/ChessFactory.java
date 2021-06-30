package builder;

import chess.Board;
import chess.BoardState;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;

public class ChessFactory {

	public Board createBoard() {
		return new Board();
	}

	public DefaultLegalMoveCalculator createDefaultLegalMoveCalculator(PosicionPiezaBoard buildDummyBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy) {
		return new DefaultLegalMoveCalculator(buildDummyBoard, buildKingCacheBoard, buildColorBoard, buildMoveCache,
				buildState, buildMoveGeneratorStrategy);
	}

	public NoCheckLegalMoveCalculator createNoCheckLegalMoveCalculator(PosicionPiezaBoard buildPosicionPiezaBoard,
			KingCacheBoard buildKingCacheBoard, ColorBoard buildColorBoard, MoveCacheBoard buildMoveCache,
			BoardState buildState, MoveGeneratorStrategy buildMoveGeneratorStrategy) {
		return new NoCheckLegalMoveCalculator(buildPosicionPiezaBoard, buildKingCacheBoard, buildColorBoard,
				buildMoveCache, buildState, buildMoveGeneratorStrategy);
	}

}
