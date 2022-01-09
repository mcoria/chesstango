package chess.layers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.BitSquareIterator;
import chess.iterators.SquareIterator;
import chess.iterators.TopDownSquareIterator;

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
		if (Color.WHITE.equals(color)) {
			squareBlancos &= ~remove.getPosicion();

			squareBlancos |= add.getPosicion();
		} else if (Color.BLACK.equals(color)) {
			squareNegros &= ~remove.getPosicion();

			squareNegros |= add.getPosicion();
		} else {
			throw new RuntimeException("Missing color");
		}
	}
	
	public void addPositions(PiecePositioned position) {
		if (Color.WHITE.equals(position.getValue().getColor())) {
			squareBlancos |= position.getKey().getPosicion();
		} else {
			squareNegros |= position.getKey().getPosicion();
		}
	}
	
	public void removePositions(PiecePositioned position){
		if(Color.WHITE.equals(position.getValue().getColor())){
			squareBlancos &= ~position.getKey().getPosicion();
		} else {
			squareNegros &= ~position.getKey().getPosicion();
		}
	}		
	

	public SquareIterator iteratorSquare(Color color){
		return Color.WHITE.equals(color) ? new BitSquareIterator(squareBlancos) : new BitSquareIterator(squareNegros);		
	}
	
	public SquareIterator iteratorSquareWhitoutKing(Color color, Square kingSquare){
		return new BitSquareIterator( (Color.WHITE.equals(color) ? squareBlancos :  squareNegros ) & ~kingSquare.getPosicion());		
	}
	
	public long getPosiciones (Color color){
		return Color.WHITE.equals(color) ? squareBlancos : squareNegros;		
	}
	
	public boolean isEmpty(Square destino) {
		return ((~(squareBlancos | squareNegros)) &  destino.getPosicion()) != 0 ;
	}	
	
	public boolean isColor(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return (squareBlancos & square.getPosicion()) != 0;
		} else if(Color.BLACK.equals(color)){
			return (squareNegros & square.getPosicion()) != 0;
		} else{
			throw new RuntimeException("Empty square");
		}
	}

	public Color getColor(Square square) {
		if ((squareBlancos & square.getPosicion()) != 0) {
			return Color.WHITE;
		} else if ((squareNegros & square.getPosicion()) != 0) {
			return Color.BLACK;
		}
		return null;
	}
	
	
	//TODO: quitar este metodo de carga, moverlo a un builder
	protected void settupSquares(PosicionPiezaBoard board) {
		for (PiecePositioned piecePositioned : board) {
			Piece piece = piecePositioned.getValue();
			if (piece != null) {
				if (Color.WHITE.equals(piece.getColor())) {
					squareBlancos |= piecePositioned.getKey().getPosicion();
				} else if (Color.BLACK.equals(piece.getColor())) {
					squareNegros |= piecePositioned.getKey().getPosicion();
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
				if(Color.WHITE.equals(color)){
					colorChr = 'X';
				} else if(Color.BLACK.equals(color)){
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


