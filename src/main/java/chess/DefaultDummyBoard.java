package chess;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import gui.ASCIIOutput;
import iterators.BoardBitSquareIterator;
import iterators.SquareIterator;

public class DefaultDummyBoard implements DummyBoard {

	public DefaultDummyBoard(Pieza[][] tablero) {
		crearTablero(tablero);
	}
	
	///////////////////////////// START positioning logic /////////////////////////////
	// Quizas podria encapsular estas operaciones en su propia clase.
	// Bitboard podria ser mas rapido? Un word por tipo de ficha
	// Las primitivas de tablero son muy basicas!? En vez de descomponer una movimiento en operaciones simples, proporcionar un solo metodo
	//
	protected PosicionPieza[] tablero = new PosicionPieza[64];
	private final CachePosiciones cachePosiciones = new CachePosiciones();
	
	/* (non-Javadoc)
	 * @see chess.DummyBoard#getPosicion(chess.Square)
	 */
	@Override
	public PosicionPieza getPosicion(Square square) {
		return tablero[square.toIdx()];
	}

	/* (non-Javadoc)
	 * @see chess.DummyBoard#setPosicion(chess.PosicionPieza)
	 */
	@Override
	public void setPosicion(PosicionPieza entry) {
		Square square = entry.getKey();
		tablero[square.toIdx()] = entry;
	}

	/* (non-Javadoc)
	 * @see chess.DummyBoard#getPieza(chess.Square)
	 */
	@Override
	public Pieza getPieza(Square square) {
		return tablero[square.toIdx()].getValue();
	}

	/* (non-Javadoc)
	 * @see chess.DummyBoard#setPieza(chess.Square, chess.Pieza)
	 */
	@Override
	public void setPieza(Square square, Pieza pieza) {
		tablero[square.toIdx()] =  cachePosiciones.getPosicion(square, pieza);
	}

	/* (non-Javadoc)
	 * @see chess.DummyBoard#setEmptySquare(chess.Square)
	 */
	@Override
	public void setEmptySquare(Square square) {
		tablero[square.toIdx()] =  cachePosiciones.getPosicion(square, null);
	}

	@Override
	public void setEmptyPosicion(PosicionPieza captura) {
		setEmptySquare(captura.getKey());
	}	
	
	/* (non-Javadoc)
	 * @see chess.DummyBoard#isEmtpy(chess.Square)
	 */
	@Override
	public boolean isEmtpy(Square square) {
		return getPieza(square) == null;
	}
	
	/* (non-Javadoc)
	 * @see chess.DummyBoard#iterator()
	 */
	@Override
	public Iterator<PosicionPieza> iterator() {
		return new Iterator<PosicionPieza>(){
			
			private int idx = 0;

			@Override
			public boolean hasNext() {
				return this.idx < 64;
			}

			@Override
			public PosicionPieza next() {
				return tablero[idx++];
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see chess.DummyBoard#iterator(iterators.SquareIterator)
	 */
	@Override
	public Iterator<PosicionPieza> iterator(SquareIterator squareIterator){
		return new Iterator<PosicionPieza>(){

			@Override
			public boolean hasNext() {
				return squareIterator.hasNext();
			}

			@Override
			public PosicionPieza next() {
				return getPosicion(squareIterator.next());
			}
			
		};
	}
	
	@Override
	public Iterator<PosicionPieza> iterator(long posiciones){
		return new BoardBitSquareIterator(tablero, posiciones);
	}	
	
	private void crearTablero(Pieza[][] sourceTablero) {
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				PosicionPieza posicion = cachePosiciones.getPosicion(Square.getSquare(file, rank),
						sourceTablero[file][rank]);
				tablero[Square.getSquare(file, rank).toIdx()] = posicion;
			}
		}
	}

	@Override
	public void move(PosicionPieza from, PosicionPieza to) {
		this.setEmptySquare(from.getKey());							//Dejamos el origen
		this.setPieza(to.getKey(), from.getValue()) ;				//Vamos al destino
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
}
