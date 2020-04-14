package moveexecutors;

import chess.Square;

@FunctionalInterface
public interface SquareKingCacheSetter {
	public void setSquareKingCache(Square square);
}
