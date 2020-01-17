package chess;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import gui.ASCIIOutput;
import iterators.BoardIterator;
import iterators.BottomUpSquareIterator;
import iterators.SquareIterator;

public class DummyBoard implements Iterable<Map.Entry<Square, Pieza>> {
	//56,57,58,59,60,61,62,63,
	//48,49,50,51,52,53,54,55,
	//40,41,42,43,44,45,46,47,
	//32,33,34,35,36,37,38,39,
	//24,25,26,27,28,29,30,31,
	//16,17,18,19,20,21,22,23,
    //08,09,10,11,12,13,14,15,
    //00,01,02,03,04,05,06,07,	
	private Pieza[][] tablero;
	
	public DummyBoard(Pieza[][] tablero){
		this.tablero = tablero;
	}

	public DummyBoard(DummyBoard theBoard) {
		tablero = new Pieza[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				tablero[i][j] = theBoard.tablero[i][j];
			}
		}
	}

	public Pieza getPieza(Square square) {
		return tablero[square.getFile()][square.getRank()];
	}
	
	public void setPieza(Square square, Pieza pieza) {
		tablero[square.getFile()][square.getRank()] = pieza;
	}
	
	public void setEmptySquare(Square square){
		tablero[square.getFile()][square.getRank()] = null;
	}

	public boolean isEmtpy(Square square) {
		return getPieza(square) == null;
	}
	
	public BoardIterator iterator(SquareIterator squareIterator){
		return new BoardIterator(){
			@Override
			public boolean hasNext() {
				return squareIterator.hasNext();
			}
			
			@Override
			public SimpleImmutableEntry<Square, Pieza> next() {
				Square currentSquare = squareIterator.next();
				Pieza pieza = getPieza(currentSquare);
				return new SimpleImmutableEntry<Square, Pieza>(currentSquare, pieza);
			}
		};
	}

	@Override
	public BoardIterator iterator() {
		return iterator(new BottomUpSquareIterator());
	}

	private Square getKingSquare(Color color) {
		Square kingSquare = null;
		for (Map.Entry<Square, Pieza> entry : this) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(currentPieza != null){
				if(currentPieza.isRey() && color.equals(currentPieza.getColor())){
					kingSquare = currentSquare;
					break;
				}
			}			
		}
		return kingSquare;
	}
	
	public boolean isKingInCheck(Color color) {
		Square kingSquare = getKingSquare(color);
		for (Map.Entry<Square, Pieza> origen : this) {
			Pieza currentPieza = origen.getValue();
			if(currentPieza != null){
				if(color.equals(currentPieza.getColor().opositeColor())){
					if(currentPieza.puedeCapturarRey(this, origen, kingSquare)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
/////////Metodo con propositos de Testing
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected Set<Move> getPseudoMoves(Color color){
		Set<Move> moves = new HashSet<Move>(){			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6937596099247521750L;

			@Override
			public String toString() {
				String str = "Size: " + this.size() + "\n";
				TreeSet<Move> sortedSet = new TreeSet<Move>(this);
				
				for (Move move : sortedSet) {
					str = str + move.getFrom().getKey().toString() + " " + move.getTo().getKey().toString() + "\n";
				}
				return str;
			};
		};
		for (Map.Entry<Square, Pieza> origen : this) {
			Pieza currentPieza = origen.getValue();
			if(currentPieza != null){
				if(color.equals(currentPieza.getColor())){
					moves.addAll(currentPieza.getPseudoMoves(this, origen));
				}
			}
		}
		return moves;
	}
	
	protected void executeMove(Square from, Square to) {
		Move move = getMovimiento(from, to);
		if(move != null) {
			move.execute(this);
		} else {
			throw new RuntimeException("Invalid move: " + from.toString() + " " + to.toString());
		}
	}	
	
	private Move getMovimiento(Square from, Square to) {
		Move moveResult = null;
		for (Move move : getPseudoMoves(this.getPieza(from).getColor())) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				moveResult = move;
			}
		}
		return moveResult;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString() {
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try (PrintStream ps = new PrintStream(baos)) {
	    	ASCIIOutput output = new ASCIIOutput(ps);
	    	output.printDummyBoard(this);
	    	ps.flush();
	    }
	    return new String(baos.toByteArray());
	}	

}
