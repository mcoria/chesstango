package net.chesstango.board.representations.ascii;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.builders.AbstractChessPositionBuilder;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.iterators.bysquare.TopDownSquareIterator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 *
 */
public class ASCIIEncoder extends AbstractChessPositionBuilder<String> {
	

	@Override
	public String getChessRepresentation() {
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try (PrintStream ps = new PrintStream(baos)) {
			getPiecePlacement(ps);
			//getState(ps);
	    }
	    return baos.toString();
	}
	

	public void getState(PrintStream printStream) {
		printStream.println("Turn: " + String.format("%-6s", turn.toString()) + ", enPassantSquare: " +  (enPassantSquare == null ? "- " : enPassantSquare.toString()) +
				", castlingWhiteQueenAllowed: " + castlingWhiteQueenAllowed +
				", castlingWhiteKingAllowed: " + castlingWhiteKingAllowed +
				", castlingBlackQueenAllowed: " + castlingBlackQueenAllowed +
				", castlingBlackKingAllowed: " + castlingBlackKingAllowed);
	}
	
	public void getPiecePlacement(PrintStream printStream) {
		SquareIterator iterator = new TopDownSquareIterator();

		printStream.println("  -------------------------------");
		do {
			Square square = iterator.next();
			
			Piece piece = board[square.getRank()][square.getFile()];

			if (square.getFile() == 0) {
				printStream.print((square.getRank() + 1));
			}

			printStream.print("| " + getChar(piece) + " ");

			if (square.getFile() == 7) {
				printStream.println("|");
				printStream.println("  -------------------------------");
			}
		} while (iterator.hasNext());

		printStream.println("   a   b   c   d   e   f   g   h");
		
		printStream.flush();
	}
	
	private char getChar(Piece piece) {
		char result = ' ';
		if(piece != null){
			switch (piece) {
			case PAWN_WHITE:
				result = 'P';
				break;
			case PAWN_BLACK:
				result = 'p';
				break;		
			case ROOK_WHITE:
				result = 'R';
				break;				
			case ROOK_BLACK:
				result = 'r';
				break;
			case KNIGHT_WHITE:
				result = 'N';
				break;				
			case KNIGHT_BLACK:
				result = 'n';
				break;
			case BISHOP_WHITE:
				result = 'B';
				break;				
			case BISHOP_BLACK:
				result = 'b';
				break;
			case QUEEN_WHITE:
				result = 'Q';
				break;				
			case QUEEN_BLACK:
				result = 'q';
				break;	
			case KING_WHITE:
				result = 'K';
				break;				
			case KING_BLACK:
				result = 'k';
				break;				
			default:
				result = '?';
				break;
			}
		}
		return result;
	}	

}
