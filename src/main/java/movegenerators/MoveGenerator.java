package movegenerators;

import java.util.Collection;

import chess.BoardCache;
import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;

public interface MoveGenerator {

	public void generateMoves(PosicionPieza origen, Collection<Move> moveContainer);

	// Creo que aca podriamos utilizar el cache, lo que nos importa es el color de los casilleros
	// Esto implica que debemos actualizar el cache siempre
	public boolean puedeCapturarPosicion(PosicionPieza origen, Square square);
	
	public void setTablero(DummyBoard tablero);

	public void setFilter(MoveFilter filter);
	
	public void setBoardCache(BoardCache boardCache); 

}
