package movegenerators;

import java.util.ArrayList;
import java.util.Collection;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;

// Y si tenemos objetos prototipos de movimientos y lo clonamos de ser validos?
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected final Color color;
	
	protected DummyBoard tablero;
	
	protected Collection<Move> moveContainer;
	
	protected Collection<Square> squareContainer;
	
	public abstract void generateMoves(PosicionPieza origen);
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public void calculatePseudoMoves(PosicionPieza origen){
		moveContainer = createContainer(); 
		squareContainer = createContainer();
		generateMoves(origen);
	}

	@Override
	public void setTablero(DummyBoard tablero) {
		this.tablero = tablero;
	}

	@Override
	public Collection<Move> getPseudoMoves(){
		return moveContainer;
	}
	
	@Override
	public Collection<Square> getAffectedBy() {
		return squareContainer;
	}
	
	private static <T> Collection<T> createContainer(){
		return new ArrayList<T>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (T move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}	
}
