package net.chesstango.board.internal.position;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.SquareIterator;
import net.chesstango.board.iterators.bysquare.PositionsSquareIterator;
import net.chesstango.board.iterators.bysquare.TopDownSquareIterator;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.SquareBoardReader;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 *
 */
public class BitBoardImp implements BitBoard {
	
	protected long squareWhites = 0;
	protected long squareBlacks = 0;

	protected long pawns = 0;
	protected long rooks = 0;
	protected long bishops = 0;
	protected long knights = 0;
	protected long queens = 0;
	protected long kings = 0;

	@Override
	public void swapPositions(Piece piece, Square remove, Square add){
		final long oldBitPosition = remove.getBitPosition();

		final long newBitPosition = add.getBitPosition();

		if (Color.WHITE.equals(piece.getColor())) {
			squareWhites = squareWhites & ~oldBitPosition | newBitPosition;
		} else {
			squareBlacks = squareBlacks & ~oldBitPosition | newBitPosition;
		}

		switch(piece){
			case PAWN_WHITE, PAWN_BLACK ->  pawns = pawns & ~oldBitPosition | newBitPosition;
			case ROOK_WHITE, ROOK_BLACK -> rooks =  rooks & ~oldBitPosition | newBitPosition;
			case BISHOP_WHITE, BISHOP_BLACK -> bishops = bishops & ~oldBitPosition | newBitPosition;
			case KNIGHT_WHITE, KNIGHT_BLACK -> knights = knights & ~oldBitPosition | newBitPosition;
			case QUEEN_WHITE, QUEEN_BLACK -> queens = queens & ~oldBitPosition | newBitPosition;
			case KING_WHITE, KING_BLACK -> kings = kings & ~oldBitPosition | newBitPosition;
		}
	}

	@Override
	public void addPosition(PiecePositioned position) {
		addPosition(position.getPiece(), position.getSquare());
	}

	@Override
	public void addPosition(Piece piece, Square square) {
		final long bitPosition = square.getBitPosition();

		if (Color.WHITE.equals(piece.getColor())) {
			squareWhites |= bitPosition;
		} else {
			squareBlacks |= bitPosition;
		}

		switch(piece){
			case PAWN_WHITE, PAWN_BLACK ->  pawns |= bitPosition;
			case ROOK_WHITE, ROOK_BLACK -> rooks |=  bitPosition;
			case BISHOP_WHITE, BISHOP_BLACK -> bishops |= bitPosition;
			case KNIGHT_WHITE, KNIGHT_BLACK -> knights |= bitPosition;
			case QUEEN_WHITE, QUEEN_BLACK -> queens |= bitPosition;
			case KING_WHITE, KING_BLACK -> kings |= bitPosition;
		}
	}

	@Override
	public void removePosition(PiecePositioned position){
		removePosition(position.getPiece(), position.getSquare());
	}

	@Override
	public void removePosition(Piece piece, Square square) {
		final long bitPosition = square.getBitPosition();

		if(Color.WHITE.equals(piece.getColor())){
			squareWhites &= ~bitPosition;
		} else {
			squareBlacks &= ~bitPosition;
		}

		switch(piece){
			case PAWN_WHITE, PAWN_BLACK ->  pawns &= ~bitPosition;
			case ROOK_WHITE, ROOK_BLACK -> rooks &= ~bitPosition;
			case BISHOP_WHITE, BISHOP_BLACK -> bishops &= ~bitPosition;
			case KNIGHT_WHITE, KNIGHT_BLACK -> knights &= ~bitPosition;
			case QUEEN_WHITE, QUEEN_BLACK -> queens &= ~bitPosition;
			case KING_WHITE, KING_BLACK -> kings &= ~bitPosition;
		}

	}


	@Override
	public SquareIterator iteratorSquare(Color color){
		return Color.WHITE.equals(color) ? new PositionsSquareIterator(squareWhites) : new PositionsSquareIterator(squareBlacks);
	}
	
	@Override
	public long getPositions(Color color){
		return Color.WHITE.equals(color) ? squareWhites : squareBlacks;		
	}

	@Override
	public long getAllPositions() {
		return squareWhites | squareBlacks;
	}

	@Override
	public long getEmptyPositions() {
		return ~(squareWhites | squareBlacks);
	}

	@Override
	public long getBishopPositions() {
		return bishops;
	}

	@Override
	public long getRookPositions() {
		return rooks;
	}

	@Override
	public long getQueenPositions() {
		return queens;
	}

	@Override
	public long getKnightPositions() {
		return knights;
	}

	@Override
	public long getPawnPositions() {
		return pawns;
	}

	@Override
	public boolean isEmpty(Square square) {
		return ((~(squareWhites | squareBlacks)) &  square.getBitPosition()) != 0 ;
	}

	@Override
	public Color getColor(Square square) {
		if ((squareWhites & square.getBitPosition()) != 0) {
			return Color.WHITE;
		} else if ((squareBlacks & square.getBitPosition()) != 0) {
			return Color.BLACK;
		}
		return null;
	}
	
	
	//TODO: quitar este metodo de carga, moverlo a un builder
	@Override
	public void init(SquareBoardReader board) {
		for (PiecePositioned piecePositioned : board) {
			if (piecePositioned.getPiece() != null) {
				addPosition(piecePositioned);
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


