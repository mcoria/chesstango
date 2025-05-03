package net.chesstango.board.representations.ascii;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.representations.AbstractPositionBuilder;
import net.chesstango.board.iterators.bysquare.SquareIterator;
import net.chesstango.board.iterators.bysquare.TopDownSquareIterator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 *
 */
public class ASCIIBuilder extends AbstractPositionBuilder<String> {
	

	@Override
	public String getPositionRepresentation() {
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
            result = switch (piece) {
                case PAWN_WHITE -> 'P';
                case PAWN_BLACK -> 'p';
                case ROOK_WHITE -> 'R';
                case ROOK_BLACK -> 'r';
                case KNIGHT_WHITE -> 'N';
                case KNIGHT_BLACK -> 'n';
                case BISHOP_WHITE -> 'B';
                case BISHOP_BLACK -> 'b';
                case QUEEN_WHITE -> 'Q';
                case QUEEN_BLACK -> 'q';
                case KING_WHITE -> 'K';
                case KING_BLACK -> 'k';
                default -> '?';
            };
		}
		return result;
	}	

}
