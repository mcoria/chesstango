package chess;

public interface Move {

	//TOOD: Y si en vez de PosicionPieza utilizamos Square ?
	PosicionPieza getFrom();
	PosicionPieza getTo();

	void executeMove(DummyBoard board);
	void undoMove(DummyBoard board);
	
	void executeMove(BoardState boardState);
	void undoMove(BoardState boardState);

	void executeMove(BoardCache boardCache);
	void undoMove(BoardCache boardCache);
	
	void executeMove(MoveCache moveCache);
	void undoMove(MoveCache moveCache);	
}