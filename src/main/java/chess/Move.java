package chess;

import layers.ColorBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;

//TOOD: Y si en vez de PosicionPieza utilizamos Square para To? Tiene sentido puesto que las capturas solo son contra piezas contrarias, sin importar que pieza es.
public interface Move {

	PosicionPieza getFrom();
	PosicionPieza getTo();

	void executeMove(PosicionPiezaBoard board);
	void undoMove(PosicionPiezaBoard board);
	
	void executeMove(BoardState boardState);
	void undoMove(BoardState boardState);

	void executeMove(ColorBoard coloBoard);
	void undoMove(ColorBoard colorBoard);
	
	void executeMove(MoveCacheBoard moveCache);
	void undoMove(MoveCacheBoard moveCache);
	
}