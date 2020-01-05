package chess;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import gui.ASCIIOutput;

public class Board {
	//56,57,58,59,60,61,62,63,
	//48,49,50,51,52,53,54,55,
	//40,41,42,43,44,45,46,47,
	//32,33,34,35,36,37,38,39,
	//24,25,26,27,28,29,30,31,
	//16,17,18,19,20,21,22,23,
    //08,09,10,11,12,13,14,15,
    //00,01,02,03,04,05,06,07,	
	private DummyBoard tablero;
	
	public Board(DummyBoard tablero){
		this.tablero = tablero;
	}
	
	public Set<Move> getMoves(Color color){
		Set<Move> moves = new HashSet<Move>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; i < 8; i++) {
				Pieza currentPieza = null; //tablero[i][j];
				if(currentPieza != null){
					if(Color.BLANCO.equals(color) && currentPieza.isBlanco()){
						moves.addAll(getMoves(currentPieza, i));
					} else if(Color.NEGRO.equals(color) && currentPieza.isNegro()) {
						moves.addAll(getMoves(currentPieza, i));
					}
				}
			}
		}
		return moves;
	}

	private Set<Move> getMoves(Pieza currentPieza, int i) {
		Set<Move> moves = null;
		if(currentPieza.isPeon()){
			moves = getPeonMoves(currentPieza, i);
		}
		return moves;
	}

	private Set<Move> getPeonMoves(Pieza currentPieza, int i) {
		if (currentPieza.isBlanco()){
			return getPeonBlancoMoves(currentPieza, i);
		} else {
			return getPeonNegroMoves(currentPieza, i);
		}
	}

	private Set<Move> getPeonNegroMoves(Pieza currentPieza, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	private Set<Move> getPeonBlancoMoves(Pieza currentPieza, int i) {
		// TODO Auto-generated method stub
		return null;
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
