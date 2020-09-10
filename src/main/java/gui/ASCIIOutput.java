package gui;

import java.io.PrintStream;
import java.util.Iterator;

import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.TopDownSquareIterator;
import layers.DummyBoard;

public class ASCIIOutput {
	
	private PrintStream printStream;
	
	public ASCIIOutput() {
		this.printStream = System.out;
	}
	
	public ASCIIOutput(PrintStream printStream) {
		this.printStream = printStream;
	}
	
	public void printDummyBoard(DummyBoard tablero) {
		Iterator<PosicionPieza> iterator = tablero.iterator(new TopDownSquareIterator());

		printStream.println("  -------------------------------");
		do {
			PosicionPieza element = iterator.next();
			Square square = element.getKey();
			Pieza pieza = element.getValue();

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
