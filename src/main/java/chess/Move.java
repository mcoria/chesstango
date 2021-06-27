package chess;

//TOOD: Y si en vez de PosicionPieza utilizamos Square para To? Tiene sentido puesto que las capturas solo son contra piezas contrarias, sin importar que pieza es.
public interface Move {

	PosicionPieza getFrom();
	PosicionPieza getTo();
	
	void execute(Board tablero);
	void undo(Board tablero);
	
	void executePseudo(Board board);
	void undoPseudo(Board board);	
}