package layers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.BitSquareIterator;
import iterators.SquareIterator;
import iterators.TopDownSquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public class ColorBoard {
	
	protected long squareBlancos = 0;
	protected long squareNegros = 0;
	
	public ColorBoard(PosicionPiezaBoard board) {
		settupSquares(board);
	}
	
	public void swapPositions(Color color, Square remove, Square add){
		if (Color.BLANCO.equals(color)) {
			squareBlancos &= ~remove.getPosicion();

			squareBlancos |= add.getPosicion();
		} else if (Color.NEGRO.equals(color)) {
			squareNegros &= ~remove.getPosicion();

			squareNegros |= add.getPosicion();
		} else {
			throw new RuntimeException("Missing color");
		}
	}
	
	public void addPositions(PosicionPieza position) {
		if (Color.BLANCO.equals(position.getValue().getColor())) {
			squareBlancos |= position.getKey().getPosicion();
		} else {
			squareNegros |= position.getKey().getPosicion();
		}
	}
	
	public void removePositions(PosicionPieza position){
		if(Color.BLANCO.equals(position.getValue().getColor())){
			squareBlancos &= ~position.getKey().getPosicion();
		} else {
			squareNegros &= ~position.getKey().getPosicion();
		}
	}		
	

	public SquareIterator iteratorSquare(Color color){
		return Color.BLANCO.equals(color) ? new BitSquareIterator(squareBlancos) : new BitSquareIterator(squareNegros);		
	}
	
	public SquareIterator iteratorSquareWhitoutKing(Color color, Square kingSquare){
		return new BitSquareIterator( (Color.BLANCO.equals(color) ? squareBlancos :  squareNegros ) & ~kingSquare.getPosicion());		
	}
	
	public long getPosiciones (Color color){
		return Color.BLANCO.equals(color) ? squareBlancos : squareNegros;		
	}
	
	public boolean isEmpty(Square destino) {
		return ((~(squareBlancos | squareNegros)) &  destino.getPosicion()) != 0 ;
	}	
	
	public boolean isColor(Color color, Square square) {
		if(Color.BLANCO.equals(color)){
			return (squareBlancos & square.getPosicion()) != 0;
		} else if(Color.NEGRO.equals(color)){
			return (squareNegros & square.getPosicion()) != 0;
		} else{
			throw new RuntimeException("Empty square");
		}
	}

	public Color getColor(Square square) {
		if ((squareBlancos & square.getPosicion()) != 0) {
			return Color.BLANCO;
		} else if ((squareNegros & square.getPosicion()) != 0) {
			return Color.NEGRO;
		}
		return null;
	}
	
	
	//TODO: quitar este metodo de carga, moverlo a un builder
	protected void settupSquares(PosicionPiezaBoard board) {
		for (PosicionPieza posicionPieza : board) {
			Pieza pieza = posicionPieza.getValue();
			if (pieza != null) {
				if (Color.BLANCO.equals(pieza.getColor())) {
					squareBlancos |= posicionPieza.getKey().getPosicion();
				} else if (Color.NEGRO.equals(pieza.getColor())) {
					squareNegros |= posicionPieza.getKey().getPosicion();
				}
			}			
		}
	}
	
	@Override
	public String toString() {
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try (PrintStream printStream = new PrintStream(baos)) {
			SquareIterator iterator = new TopDownSquareIterator();
	
			printStream.println("  -------------------------------");
			do {
				Square square = iterator.next();
				
				char colorChr = ' ';
				
				Color color = this.getColor(square);
				if(Color.BLANCO.equals(color)){
					colorChr = 'X';
				} else if(Color.NEGRO.equals(color)){
					colorChr = 'O';
				}
	
				if (square.getFile() == 0) {
					printStream.print((square.getRank() + 1));
				}
	
				
				printStream.print("| " + colorChr + " ");
	
				if (square.getFile() == 7) {
					printStream.println("|");
					printStream.println("  -------------------------------");
				}
			} while (iterator.hasNext());
			printStream.println("   a   b   c   d   e   f   g   h");
			printStream.flush();
	    }
	    return new String(baos.toByteArray());
	}	
	
}


