package chess;

import layers.ColorBoard;
import layers.DummyBoard;
import layers.MoveCacheBoard;

//TOOD: Y si en vez de PosicionPieza utilizamos Square para To? Tiene sentido puesto que las capturas solo son contra piezas contrarias, sin importar que pieza es.
public interface Move {

	PosicionPieza getFrom();
	PosicionPieza getTo();

	void executeMove(DummyBoard board);
	void undoMove(DummyBoard board);
	
	void executeMove(BoardState boardState);
	void undoMove(BoardState boardState);

	void executeMove(ColorBoard coloBoard);
	void undoMove(ColorBoard colorBoard);
	
	void executeMove(MoveCacheBoard moveCache);
	void undoMove(MoveCacheBoard moveCache);	
}