package chess.gui;

import java.io.PrintStream;

import chess.Color;
import chess.Pieza;
import chess.Square;
import chess.builder.ChessBuilder;
import chess.iterators.SquareIterator;
import chess.iterators.TopDownSquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public class ASCIIOutput implements ChessBuilder {
	
	private Pieza[][] tablero = new Pieza[8][8];
	
	public void printDummyBoard(PrintStream printStream) {
		SquareIterator iterator = new TopDownSquareIterator();

		printStream.println("  -------------------------------");
		do {
			Square square = iterator.next();
			
			Pieza pieza = tablero[square.getRank()][square.getFile()];

			if (square.getFile() == 0) {
				printStream.print((square.getRank() + 1));
			}

			printStream.print("| " + getChar(pieza) + " ");

			if (square.getFile() == 7) {
				printStream.println("|");
				printStream.println("  -------------------------------");
			}
		} while (iterator.hasNext());

		printStream.println("   a   b   c   d   e   f   g   h");
		
		printStream.flush();
	}
	
	
	@Override
	public void withPieza(Square square, Pieza pieza) {
		tablero[square.getRank()][square.getFile()] = pieza;
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
	public void withCastlingWhiteReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withCastlingBlackReinaPermitido(boolean enroqueNegroReinaPermitido) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withCastlingBlackKingPermitido(boolean enroqueNegroKingPermitido) {
		// TODO Auto-generated method stub
		
	}
	
	private char getChar(Pieza pieza) {
		char result = ' ';
		if(pieza != null){
			switch (pieza) {
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
