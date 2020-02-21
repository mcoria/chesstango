package chess;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import gui.ASCIIOutput;
import iterators.BoardIterator;
import iterators.DummyBoardIterator;
import iterators.SquareIterator;
import movegenerators.MoveFilter;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;

public class DummyBoard implements Iterable<Map.Entry<Square, Pieza>>, MoveFilter {
	
	public static final Map.Entry<Square, Pieza> TORRE_NEGRO_REYNA = new SimpleImmutableEntry<Square, Pieza>(Square.a8, Pieza.TORRE_NEGRO);
	public static final Map.Entry<Square, Pieza> REY_NEGRO = new SimpleImmutableEntry<Square, Pieza>(Square.e8, Pieza.REY_NEGRO);
	public static final Map.Entry<Square, Pieza> TORRE_NEGRO_REY = new SimpleImmutableEntry<Square, Pieza>(Square.h8, Pieza.TORRE_NEGRO);
	
	public static final Map.Entry<Square, Pieza> TORRE_BLANCA_REYNA = new SimpleImmutableEntry<Square, Pieza>(Square.a1, Pieza.TORRE_BLANCO);
	public static final Map.Entry<Square, Pieza> REY_BLANCO = new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO);
	public static final Map.Entry<Square, Pieza> TORRE_BLANCA_REY = new SimpleImmutableEntry<Square, Pieza>(Square.h1, Pieza.TORRE_BLANCO);
	
	private BoardState boardState;
	private MoveGeneratorStrategy strategy;
	
	//56,57,58,59,60,61,62,63,
	//48,49,50,51,52,53,54,55,
	//40,41,42,43,44,45,46,47,
	//32,33,34,35,36,37,38,39,
	//24,25,26,27,28,29,30,31,
	//16,17,18,19,20,21,22,23,
    //08,09,10,11,12,13,14,15,
    //00,01,02,03,04,05,06,07,	
	private Pieza[][] tablero;
	
	//@SuppressWarnings("unchecked")
	//private Map.Entry<Square, Pieza>[][] tablero = new Map.Entry[8][8];
	
	private final CachePosiciones cachePosiciones = new CachePosiciones();
	
	public DummyBoard(Pieza[][] tablero, BoardState boardState) {
		//crearTablero(tablero);
		this.tablero = tablero;
		this.boardState = boardState;
		this.strategy = new MoveGeneratorStrategy(this);
	}

	/*
	private void crearTablero(Pieza[][] sourceTablero) {		
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				tablero[file][rank] =  new SimpleImmutableEntry<Square, Pieza>(Square.getSquare(file, rank), sourceTablero[file][rank]);
				//cachePosiciones.getPosicion(Square.getSquare(file, rank), sourceTablero[file][rank]);
			}
		}
	}*/
	
	public Map.Entry<Square, Pieza> getPosicion(Square square) {
		return cachePosiciones.getPosicion(square, tablero[square.getFile()][square.getRank()]);
		//new SimpleImmutableEntry<Square, Pieza>(square, tablero[square.getFile()][square.getRank()]);
	}

	public void setPosicion(Map.Entry<Square, Pieza> entry) {
		tablero[entry.getKey().getFile()][entry.getKey().getRank()] = entry.getValue();
	}

	public Pieza getPieza(Square square) {
		return tablero[square.getFile()][square.getRank()];
	}

	public void setPieza(Square square, Pieza pieza) {
		tablero[square.getFile()][square.getRank()] = pieza;
	}

	public void setEmptySquare(Square square) {
		tablero[square.getFile()][square.getRank()] = null;
	}

	public boolean isEmtpy(Square square) {
		return getPieza(square) == null;
	}
	
	public Collection<Move>  getLegalMoves(){
		Collection<Move> moves = createMoveContainer();
		for (Map.Entry<Square, Pieza> origen : this) {
			Pieza currentPieza = origen.getValue();
			if(currentPieza != null){
				if(boardState.getTurnoActual().equals(currentPieza.getColor())){
					MoveGenerator moveGenerator = strategy.getMoveGenerator(currentPieza);
					moves.addAll(moveGenerator.generateMoves(origen));
				}
			}
		}
		return moves;
	}
	
	@Override
	public void filterMove(Collection<Move> moves, Move move) {
		Color turno = boardState.getTurnoActual();
		move.execute(this);
		if(! this.isKingInCheck(turno) ) {
			moves.add(move);
		}
		move.undo(this);
		boardState.restoreState();
	}

	public boolean isKingInCheck(Color color) {
		Square kingSquare = getKingSquare(color);
		return sepuedeCapturarReyEnSquare(color, kingSquare);
	}	
	
	public boolean sepuedeCapturarReyEnSquare(Color color, Square kingSquare){
		for (Map.Entry<Square, Pieza> origen : this) {
			Pieza currentPieza = origen.getValue();
			if(currentPieza != null){
				if(color.equals(currentPieza.getColor().opositeColor())){
					MoveGenerator moveGenerator = strategy.getMoveGenerator(currentPieza);
					if(moveGenerator.puedeCapturarRey(this, origen, kingSquare)){
						return true;
					}
				}
			}
		}
		return false;		
	}
	
	private Square squareKingCache = Square.e1;
	
	private Square getKingSquare(Color color) {
		Pieza rey = Pieza.getRey(color);
		Pieza posiblePieza = this.getPieza(squareKingCache);
		if(rey.equals(posiblePieza)){
			return squareKingCache;
		}		
		return getKingSquareRecorrer(color);
	}
	
	private Square getKingSquareRecorrer(Color color) {
		Square kingSquare = null;
		Pieza rey = Pieza.getRey(color);
		for (Map.Entry<Square, Pieza> entry : this) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(rey.equals(currentPieza)){
				kingSquare = currentSquare;
				squareKingCache = currentSquare;
				break;
			}
		}
		return kingSquare;
	}

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
	
	private Collection<Move> createMoveContainer(){
		return new ArrayList<Move>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (Move move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}
	
	public BoardIterator iterator(SquareIterator squareIterator){
		return new BoardIterator(){
			@Override
			public boolean hasNext() {
				return squareIterator.hasNext();
			}
			
			@Override
			public Map.Entry<Square, Pieza> next() {
				Square currentSquare = squareIterator.next();
				return getPosicion(currentSquare);
			}
		};
	}

	@Override
	public BoardIterator iterator() {
		return new DummyBoardIterator(this);
	}	

	public BoardState getBoardState() {
		return boardState;
	}

	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}	

}
