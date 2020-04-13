package chess;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gui.ASCIIOutput;
import iterators.BoardIterator;
import iterators.DummyBoardIterator;
import iterators.SquareIterator;
import movegenerators.MoveFilter;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;

public class DummyBoard implements Iterable<Map.Entry<Square, Pieza>> {
	
	public static final Map.Entry<Square, Pieza> TORRE_NEGRO_REYNA = new SimpleImmutableEntry<Square, Pieza>(Square.a8, Pieza.TORRE_NEGRO);
	public static final Map.Entry<Square, Pieza> REY_NEGRO = new SimpleImmutableEntry<Square, Pieza>(Square.e8, Pieza.REY_NEGRO);
	public static final Map.Entry<Square, Pieza> TORRE_NEGRO_REY = new SimpleImmutableEntry<Square, Pieza>(Square.h8, Pieza.TORRE_NEGRO);
	
	public static final Map.Entry<Square, Pieza> TORRE_BLANCA_REYNA = new SimpleImmutableEntry<Square, Pieza>(Square.a1, Pieza.TORRE_BLANCO);
	public static final Map.Entry<Square, Pieza> REY_BLANCO = new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO);
	public static final Map.Entry<Square, Pieza> TORRE_BLANCA_REY = new SimpleImmutableEntry<Square, Pieza>(Square.h1, Pieza.TORRE_BLANCO);
	
	//56,57,58,59,60,61,62,63,
	//48,49,50,51,52,53,54,55,
	//40,41,42,43,44,45,46,47,
	//32,33,34,35,36,37,38,39,
	//24,25,26,27,28,29,30,31,
	//16,17,18,19,20,21,22,23,
    //08,09,10,11,12,13,14,15,
    //00,01,02,03,04,05,06,07,	
	//private Pieza[][] tablero;
	
	@SuppressWarnings("unchecked")
	private Map.Entry<Square, Pieza>[] tablero = new Map.Entry[64];
	private final CachePosiciones cachePosiciones = new CachePosiciones();
	
	private List<Square> squareBlancos = new ArrayList<Square>();
	private List<Square> squareNegros = new ArrayList<Square>();
	
	private MoveGeneratorStrategy strategy = new MoveGeneratorStrategy(this, (Collection<Move> moves, Move move) -> filterMove(moves, move) );
	
	private BoardState boardState = null;

	
	public DummyBoard(Pieza[][] tablero, BoardState boardState) {
		crearTablero(tablero);
		this.boardState = boardState;
	}
	
	
	///////////////////////////// START positioning logic /////////////////////////////
	public Map.Entry<Square, Pieza> getPosicion(Square square) {
		return tablero[square.toIdx()];
	}

	public void setPosicion(Map.Entry<Square, Pieza> entry) {
		Square square = entry.getKey();
		tablero[square.toIdx()] = entry;
	}

	public Pieza getPieza(Square square) {
		return tablero[square.toIdx()].getValue();
	}

	public void setPieza(Square square, Pieza pieza) {
		tablero[square.toIdx()] =  cachePosiciones.getPosicion(square, pieza);
	}

	public void setEmptySquare(Square square) {
		tablero[square.toIdx()] =  cachePosiciones.getPosicion(square, null);
	}

	public boolean isEmtpy(Square square) {
		return getPieza(square) == null;
	}
	///////////////////////////// END positioning logic /////////////////////////////
	
	public Collection<Move>  getLegalMoves(){
		Collection<Move> moves = createMoveContainer();
		Color turnoActual = boardState.getTurnoActual();
		for (SquareIterator iterator = this.iteratorSquare(turnoActual); iterator.hasNext();) {
			Entry<Square, Pieza> origen = this.getPosicion(iterator.next());
			Pieza currentPieza = origen.getValue();
			MoveGenerator moveGenerator = strategy.getMoveGenerator(currentPieza);
			moveGenerator.generateMoves(origen, moves);
		}
		return moves;
	}
	
	private void filterMove(Collection<Move> moves, Move move) {
		move.executeMove(this);
		if(! this.isKingInCheck() ) {
			moves.add(move);
		}
		move.undoMove(this);
	}

	public boolean isKingInCheck() {
		Color turno = boardState.getTurnoActual();
		Square kingSquare = getKingSquare(turno);
		return sepuedeCapturarReyEnSquare(turno, kingSquare);
	}	
	
	public boolean sepuedeCapturarReyEnSquare(Color colorRey, Square kingSquare){
		for (SquareIterator iterator = this.iteratorSquare(colorRey.opositeColor()); iterator.hasNext();) {
			Entry<Square, Pieza> origen = this.getPosicion(iterator.next());
			Pieza currentPieza = origen.getValue();
			if(currentPieza != null){
				if(colorRey.equals(currentPieza.getColor().opositeColor())){
					MoveGenerator moveGenerator = strategy.getMoveGenerator(currentPieza);
					if(moveGenerator.puedeCapturarRey(origen, kingSquare)){
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

	///////////////////////////// START Board Iteration Logic /////////////////////////////	
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
	
	protected SquareIterator iteratorSquare(Color color){
		return new SquareIterator(){
			private Iterator<Square> iterator = Color.BLANCO.equals(color) ? squareBlancos.iterator() : squareNegros.iterator();
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}
			
			@Override
			public Square next() {
				return iterator.next();
			}
		};		
	}

	@Override
	public BoardIterator iterator() {
		return new DummyBoardIterator(this);
	}

	///////////////////////////// END Board Iteration Logic /////////////////////////////	

	///////////////////////////// START Move execution Logic /////////////////////////////		
	public void execute(Move move) {
		move.executeMove(this);
		
		List<Square> squaresTurno = Color.BLANCO.equals(boardState.getTurnoActual()) ? this.squareBlancos : this.squareNegros;
		List<Square> squaresOpenente = Color.BLANCO.equals(boardState.getTurnoActual()) ? this.squareNegros : this.squareBlancos;
		move.executeSquareLists(squaresTurno, squaresOpenente);
		
		move.executeState(boardState);
		
		//assert validarSquares(squareBlancos, Color.BLANCO) && validarSquares(squareNegros, Color.NEGRO);
	}


	public void undo(Move move) {
		move.undoMove(this);
		
		List<Square> squaresTurno = Color.BLANCO.equals(boardState.getTurnoActual()) ? this.squareNegros : this.squareBlancos;
		List<Square> squaresOpenente = Color.BLANCO.equals(boardState.getTurnoActual()) ? this.squareBlancos : this.squareNegros;
		move.undoSquareLists(squaresTurno, squaresOpenente);	
		
		move.undoState(boardState);
		
		//assert validarSquares(squareBlancos, Color.BLANCO) && validarSquares(squareNegros, Color.NEGRO);
	}
	///////////////////////////// END Move execution Logic /////////////////////////////
	
	
	public BoardState getBoardState() {
		return boardState;
	}
	
	public MoveFilter getDefaultFilter(){
		return (Collection<Move> moves, Move move) -> filterMove(moves, move);
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
	
	private static Collection<Move> createMoveContainer(){
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
	
	private void crearTablero(Pieza[][] sourceTablero) {
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				Entry<Square, Pieza> posicion = cachePosiciones.getPosicion(Square.getSquare(file, rank),
						sourceTablero[file][rank]);
				tablero[Square.getSquare(file, rank).toIdx()] = posicion;

				Pieza pieza = posicion.getValue();
				if (pieza != null) {
					if (Color.BLANCO.equals(pieza.getColor())) {
						squareBlancos.add(posicion.getKey());
					} else if (Color.NEGRO.equals(pieza.getColor())) {
						squareNegros.add(posicion.getKey());
					}
				}
			}
		}
	}	

}
