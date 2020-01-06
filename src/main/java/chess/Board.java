package chess;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gui.ASCIIOutput;

public class Board {
	private DummyBoard tablero;
	
	public Board(DummyBoard tablero){
		this.tablero = tablero;
	}
	
	public Set<Move> getMoves(Color color){
		Set<Move> moves = new HashSet<Move>();
		for (Map.Entry<Square, Pieza> entry : tablero) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(currentPieza != null){
				if(color.equals(currentPieza.getColor())){
					moves.addAll(currentPieza.getLegalMoves(tablero, currentSquare));
				}
			}
		}
		return moves;
	}

	public final DummyBoard getTablero() {
		return tablero;
	}

	public void setTablero(DummyBoard tablero) {
		this.tablero = tablero;
	}
	
	@Override
	public String toString() {
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try (PrintStream ps = new PrintStream(baos)) {
	    	ASCIIOutput output = new ASCIIOutput(ps);
	    	output.printBoard(this);
	    	ps.flush();
	    }
	    return new String(baos.toByteArray());
	}
}
