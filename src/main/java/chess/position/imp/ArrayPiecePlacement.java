package chess.position.imp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import chess.CachePosiciones;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.gui.ASCIIOutput;
import chess.iterators.pieceplacement.BoardBitIterator;
import chess.iterators.pieceplacement.PiecePlacementIterator;
import chess.iterators.square.SquareIterator;
import chess.position.PiecePlacement;

/**
 * @author Mauricio Coria
 *
 */
public class ArrayPiecePlacement implements PiecePlacement, Cloneable  {
	
	public ArrayPiecePlacement(){
		for(int i = 0; i < 64; i++){
			this.setEmptySquare(Square.getSquare(i));
		}		
	}
	
	///////////////////////////// START positioning logic /////////////////////////////
	// Quizas podria encapsular estas operaciones en su propia clase.
	// Bitboard podria ser mas rapido? Un word por tipo de ficha
	// Las primitivas de tablero son muy basicas!? En vez de descomponer una movimiento en operaciones simples, proporcionar un solo metodo
	//
	protected PiecePositioned[] tablero = new PiecePositioned[64];
	private final CachePosiciones cachePosiciones = new CachePosiciones();
	

	@Override
	public PiecePositioned getPosicion(Square square) {
		return tablero[square.toIdx()];
	}


	@Override
	public void setPosicion(PiecePositioned entry) {
		Square square = entry.getKey();
		tablero[square.toIdx()] = entry;
	}


	@Override
	public Piece getPieza(Square square) {
		return tablero[square.toIdx()].getValue();
	}


	@Override
	public void setPieza(Square square, Piece piece) {
		tablero[square.toIdx()] =  cachePosiciones.getPosicion(square, piece);
	}


	@Override
	public void setEmptySquare(Square square) {
		tablero[square.toIdx()] =  cachePosiciones.getPosicion(square, null);
	}

	@Override
	public void setEmptyPosicion(PiecePositioned captura) {
		setEmptySquare(captura.getKey());
	}	
	

	@Override
	public boolean isEmtpy(Square square) {
		return getPieza(square) == null;
	}
	

	@Override
	public PiecePlacementIterator iterator() {
		return new PiecePlacementIterator(){
			
			private int idx = 0;

			@Override
			public boolean hasNext() {
				return this.idx < 64;
			}

			@Override
			public PiecePositioned next() {
				return tablero[idx++];
			}
			
		};
	}


	@Override
	public PiecePlacementIterator iterator(SquareIterator squareIterator){
		return new PiecePlacementIterator(){

			@Override
			public boolean hasNext() {
				return squareIterator.hasNext();
			}

			@Override
			public PiecePositioned next() {
				return getPosicion(squareIterator.next());
			}
			
		};
	}
	
	@Override
	public PiecePlacementIterator iterator(long posiciones){
		return new BoardBitIterator(tablero, posiciones);
	}	

	@Override
	public void move(PiecePositioned from, PiecePositioned to) {
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
			output.getPiecePlacement(ps);
	    }
	    return new String(baos.toByteArray());
	}
	
	
	@Override
	public ArrayPiecePlacement clone() throws CloneNotSupportedException {
		ArrayPiecePlacement clone = new ArrayPiecePlacement();
		for(int i = 0; i < 64; i++){
			clone.tablero[i] = this.tablero[i];
		}
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ArrayPiecePlacement){
			ArrayPiecePlacement theInstance = (ArrayPiecePlacement) obj;
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
