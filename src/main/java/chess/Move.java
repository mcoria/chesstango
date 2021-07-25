package chess;

import layers.ColorBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.MoveFilter;

//TOOD: Y si en vez de PosicionPieza utilizamos Square para To?
//      La mayoria de los movimientos posibles es a square vacios
//      Tiene sentido puesto que las capturas solo son contra piezas contrarias, sin importar que pieza es.

//TODO: y se implementamos un cache de movimientos?
public interface Move extends Comparable<Move> {

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
	
	
	void executeMove(Board board);
	void undoMove(Board board);
	
	
	boolean filer(MoveFilter filter);
	
}