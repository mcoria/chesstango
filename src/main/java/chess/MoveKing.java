package chess;

public interface MoveKing extends Move {
	public void executetSquareKingCache(BoardCache cache);

	public void undoSquareKingCache(BoardCache cache);
}
