package gui;

import java.io.PrintStream;

import builder.ChessBuilder;
import chess.Color;
import chess.Pieza;
import chess.Square;
import iterators.SquareIterator;
import iterators.TopDownSquareIterator;

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
	public void withPeonPasanteSquare(Square peonPasanteSquare) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		// TODO Auto-generated method stub
		
	}
	
	private char getChar(Pieza pieza) {
		char result = ' ';
		if(pieza != null){
			switch (pieza) {
			case PEON_BLANCO:
				result = 'P';
				break;
			case PEON_NEGRO:
				result = 'p';
				break;		
			case TORRE_BLANCO:
				result = 'R';
				break;				
			case TORRE_NEGRO:
				result = 'r';
				break;
			case CABALLO_BLANCO:
				result = 'N';
				break;				
			case CABALLO_NEGRO:
				result = 'n';
				break;
			case ALFIL_BLANCO:
				result = 'B';
				break;				
			case ALFIL_NEGRO:
				result = 'b';
				break;
			case REINA_BLANCO:
				result = 'Q';
				break;				
			case REINA_NEGRO:
				result = 'q';
				break;	
			case REY_BLANCO:
				result = 'K';
				break;				
			case REY_NEGRO:
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
