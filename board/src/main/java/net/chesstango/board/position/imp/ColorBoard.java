package net.chesstango.board.position.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.iterators.bysquare.PositionsSquareIterator;
import net.chesstango.board.iterators.bysquare.TopDownSquareIterator;
import net.chesstango.board.position.BoardReader;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 *
 */
public class ColorBoard {
	
	protected long squareWhites = 0;
	protected long squareBlacks = 0;
	
	public void swapPositions(Color color, Square remove, Square add){
		if (Color.WHITE.equals(color)) {
			squareWhites &= ~remove.getBitPosition();

			squareWhites |= add.getBitPosition();
		} else if (Color.BLACK.equals(color)) {
			squareBlacks &= ~remove.getBitPosition();

			squareBlacks |= add.getBitPosition();
		} else {
			throw new RuntimeException("Missing color");
		}
	}
	
	public void addPositions(PiecePositioned position) {
		if (Color.WHITE.equals(position.getPiece().getColor())) {
			squareWhites |= position.getSquare().getBitPosition();
		} else {
			squareBlacks |= position.getSquare().getBitPosition();
		}
	}
	
	public void removePositions(PiecePositioned position){
		if(Color.WHITE.equals(position.getPiece().getColor())){
			squareWhites &= ~position.getSquare().getBitPosition();
		} else {
			squareBlacks &= ~position.getSquare().getBitPosition();
		}
	}		
	

	public SquareIterator iteratorSquare(Color color){
		return Color.WHITE.equals(color) ? new PositionsSquareIterator(squareWhites) : new PositionsSquareIterator(squareBlacks);
	}
	
	public SquareIterator iteratorSquareWithoutKing(Color color, Square kingSquare){
		return new PositionsSquareIterator( (Color.WHITE.equals(color) ? squareWhites :  squareBlacks ) & ~kingSquare.getBitPosition());
	}
	
	public long getPosiciones (Color color){
		return Color.WHITE.equals(color) ? squareWhites : squareBlacks;		
	}
	
	public boolean isEmpty(Square destino) {
		return ((~(squareWhites | squareBlacks)) &  destino.getBitPosition()) != 0 ;
	}	
	
	public boolean isColor(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return (squareWhites & square.getBitPosition()) != 0;
		} else if(Color.BLACK.equals(color)){
			return (squareBlacks & square.getBitPosition()) != 0;
		} else{
			throw new RuntimeException("Empty bysquare");
		}
	}

	public Color getColor(Square square) {
		if ((squareWhites & square.getBitPosition()) != 0) {
			return Color.WHITE;
		} else if ((squareBlacks & square.getBitPosition()) != 0) {
			return Color.BLACK;
		}
		return null;
	}
	
	
	//TODO: quitar este metodo de carga, moverlo a un builder
	public void init(BoardReader board) {
		for (PiecePositioned piecePositioned : board) {
			Piece piece = piecePositioned.getPiece();
			if (piece != null) {
				if (Color.WHITE.equals(piece.getColor())) {
					squareWhites |= piecePositioned.getSquare().getBitPosition();
				} else if (Color.BLACK.equals(piece.getColor())) {
					squareBlacks |= piecePositioned.getSquare().getBitPosition();
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
	    return baos.toString();
	}	
	
}


