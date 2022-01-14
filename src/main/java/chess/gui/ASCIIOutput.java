package chess.gui;

import java.io.PrintStream;

import chess.Color;
import chess.Piece;
import chess.Square;
import chess.builder.ChessPositionBuilder;
import chess.iterators.square.SquareIterator;
import chess.iterators.square.TopDownSquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public class ASCIIOutput implements ChessPositionBuilder {
	
	private Piece[][] tablero = new Piece[8][8];
	
	public void printDummyBoard(PrintStream printStream) {
		SquareIterator iterator = new TopDownSquareIterator();

		printStream.println("  -------------------------------");
		do {
			Square square = iterator.next();
			
			Piece piece = tablero[square.getRank()][square.getFile()];

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
	
	
	@Override
	public void withPieza(Square square, Piece piece) {
		tablero[square.getRank()][square.getFile()] = piece;
	}


	@Override
	public void withTurno(Color turno) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withPawnPasanteSquare(Square peonPasanteSquare) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		// TODO Auto-generated method stub
		
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
