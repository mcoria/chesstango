package layers.imp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import chess.CachePosiciones;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import gui.ASCIIOutput;
import iterators.SquareIterator;
import iterators.posicionpieza.BoardBitIterator;
import layers.PosicionPiezaBoard;

/**
 * @author Mauricio Coria
 *
 */
public class ArrayPosicionPiezaBoard implements PosicionPiezaBoard, Cloneable  {
	
	public ArrayPosicionPiezaBoard(){
		for(int i = 0; i < 64; i++){
			this.setEmptySquare(Square.getSquare(i));
		}		
	}
	
	///////////////////////////// START positioning logic /////////////////////////////
	// Quizas podria encapsular estas operaciones en su propia clase.
	// Bitboard podria ser mas rapido? Un word por tipo de ficha
	// Las primitivas de tablero son muy basicas!? En vez de descomponer una movimiento en operaciones simples, proporcionar un solo metodo
	//
	protected PosicionPieza[] tablero = new PosicionPieza[64];
	private final CachePosiciones cachePosiciones = new CachePosiciones();
	

	@Override
	public PosicionPieza getPosicion(Square square) {
		return tablero[square.toIdx()];
	}


	@Override
	public void setPosicion(PosicionPieza entry) {
		Square square = entry.getKey();
		tablero[square.toIdx()] = entry;
	}


	@Override
	public Pieza getPieza(Square square) {
		return tablero[square.toIdx()].getValue();
	}


	@Override
	public void setPieza(Square square, Pieza pieza) {
		tablero[square.toIdx()] =  cachePosiciones.getPosicion(square, pieza);
	}


	@Override
	public void setEmptySquare(Square square) {
		tablero[square.toIdx()] =  cachePosiciones.getPosicion(square, null);
	}

	@Override
	public void setEmptyPosicion(PosicionPieza captura) {
		setEmptySquare(captura.getKey());
	}	
	

	@Override
	public boolean isEmtpy(Square square) {
		return getPieza(square) == null;
	}
	

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
		return new BoardBitIterator(tablero, posiciones);
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
	    	ASCIIOutput output = new ASCIIOutput();
			this.forEach(posicionPieza -> {
				output.withPieza(posicionPieza.getKey(), posicionPieza.getValue());
			});
			output.printDummyBoard(ps);
	    }
	    return new String(baos.toByteArray());
	}
	
	
	@Override
	public ArrayPosicionPiezaBoard clone() throws CloneNotSupportedException {
		ArrayPosicionPiezaBoard clone = new ArrayPosicionPiezaBoard();
		for(int i = 0; i < 64; i++){
			clone.tablero[i] = this.tablero[i];
		}
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ArrayPosicionPiezaBoard){
			ArrayPosicionPiezaBoard theInstance = (ArrayPosicionPiezaBoard) obj;
			for(int i = 0; i < 64; i++){
				if(! this.tablero[i].equals(theInstance.tablero[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}	

}
