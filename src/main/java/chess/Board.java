package chess;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
import moveexecutors.SquareKingCacheSetter;
import movegenerators.MoveFilter;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;

public class Board implements Iterable<Map.Entry<Square, Pieza>> {
	
	private MoveFilter defaultFilter = (Collection<Move> moves, Move move) -> filterMove(moves, move);
	
	private MoveGeneratorStrategy strategy = new MoveGeneratorStrategy(this, this.defaultFilter);
	
	private BoardState boardState = null;

	public Board(Pieza[][] tablero, BoardState boardState) {
		crearTablero(tablero);
		this.boardState = boardState;
		setSquareKingBlancoCache(getKingSquareRecorrer(Color.BLANCO));
		setSquareKingNegroCache(getKingSquareRecorrer(Color.NEGRO));
	}
	

	///////////////////////////// START positioning logic /////////////////////////////
	// Quizas podria encapsular estas operaciones en su propia clase.
	// Bitboard podria ser mas rapido? Un word por tipo de ficha
	// Las primitivas de tablero son muy basicas!? En vez de descomponer una movimiento en operaciones simples, proporcionar un solo metodo
	// 
	@SuppressWarnings("unchecked")
	private Map.Entry<Square, Pieza>[] tablero = new Map.Entry[64];
	private final CachePosiciones cachePosiciones = new CachePosiciones();
	
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
	
	public Collection<Move> getLegalMoves(){
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
					MoveGenerator moveGenerator = this.strategy.getMoveGenerator(currentPieza);
					if(moveGenerator.puedeCapturarRey(origen, kingSquare)){
						return true;
					}
				}
			}
		}
		return false;		
	}
	
	private void filterMove(Collection<Move> moves, Move move) {
		move.executeMove(this);
		if(! this.isKingInCheck() ) {
			moves.add(move);
		}
		move.undoMove(this);
	}

	///////////////////////////// START getKingSquare Logic /////////////////////////////
	private Square squareKingBlancoCache = null;
	private Square squareKingNegroCache = null;
	
	private SquareKingCacheSetter kingBlancoSetter = (Square square) -> setSquareKingBlancoCache(square);
	private SquareKingCacheSetter kingNegroSetter = (Square square) -> setSquareKingNegroCache(square);
	
	public SquareKingCacheSetter getSquareKingCacheSetter(Color color){
		return Color.BLANCO.equals(color) ? kingBlancoSetter : kingNegroSetter;
	}
	
	private Square getKingSquare(Color color) {
		return Color.BLANCO.equals(color) ? squareKingBlancoCache : squareKingNegroCache;
	}
	
	private void setSquareKingBlancoCache(Square square){
		this.squareKingBlancoCache = square;
	}
	
	private void setSquareKingNegroCache(Square square){
		this.squareKingNegroCache = square;
	}
	
	private Square getKingSquareRecorrer(Color color) {
		Square kingSquare = null;
		Pieza rey = Pieza.getRey(color);
		for (Map.Entry<Square, Pieza> entry : this) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(rey.equals(currentPieza)){
				kingSquare = currentSquare;
				break;
			}
		}
		return kingSquare;
	}	
	///////////////////////////// END getKingSquare Logic /////////////////////////////	

	///////////////////////////// START Board Iteration Logic /////////////////////////////
	@Override
	public BoardIterator iterator() {
		return new DummyBoardIterator(this);
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
	///////////////////////////// END Board Iteration Logic /////////////////////////////	
	
	///////////////////////////// START Cache Iteration Logic /////////////////////////////	
	// Bien podriamos encapsular este cache en su propia clase
	// Prestar atencion que este cache se actualiza una vez que realmente se mueven las fichas
	private List<Square> squareBlancos = new ArrayList<Square>();
	private List<Square> squareNegros = new ArrayList<Square>();
	
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
	///////////////////////////// START Cache Iteration Logic /////////////////////////////	

	///////////////////////////// START Move execution Logic /////////////////////////////		
	public void execute(Move move) {
		move.executeMove(this);
		
		List<Square> squaresTurno = Color.BLANCO.equals(boardState.getTurnoActual()) ? this.squareBlancos : this.squareNegros;
		List<Square> squaresOpenente = squaresTurno == this.squareBlancos ? this.squareNegros : this.squareBlancos;
		move.executeSquareLists(squaresTurno, squaresOpenente);
		
		move.executeState(boardState);
		
		//assert validarSquares(squareBlancos, Color.BLANCO) && validarSquares(squareNegros, Color.NEGRO);
	}


	public void undo(Move move) {
		move.undoMove(this);
		
		List<Square> squaresTurno = Color.BLANCO.equals(boardState.getTurnoActual()) ? this.squareNegros : this.squareBlancos;
		List<Square> squaresOpenente = squaresTurno == this.squareBlancos ? this.squareNegros : this.squareBlancos;
		move.undoSquareLists(squaresTurno, squaresOpenente);	
		
		move.undoState(boardState);
		
		//assert validarSquares(squareBlancos, Color.BLANCO) && validarSquares(squareNegros, Color.NEGRO);
	}
	///////////////////////////// END Move execution Logic /////////////////////////////
	
	
	public BoardState getBoardState() {
		return boardState;
	}
	
	public MoveFilter getDefaultFilter(){
		return defaultFilter;
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
