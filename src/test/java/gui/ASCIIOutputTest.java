package gui;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import chess.Board;
import parsers.FENParser;

public class ASCIIOutputTest {

	@Test
	public void test() {	    
	    // Expected
	    final ByteArrayOutputStream baosExp = new ByteArrayOutputStream();
	    try (PrintStream ps = new PrintStream(baosExp)) {
	    	ps.println("  -------------------------------");
	    	ps.println("8| r | n | b | q | k | b | n | r |");
	    	ps.println("  -------------------------------");
			ps.println("7| p | p | p | p | p | p | p | p |");
			ps.println("  -------------------------------");
			ps.println("6|   |   |   |   |   |   |   |   |");
			ps.println("  -------------------------------");
			ps.println("5|   |   |   |   |   |   |   |   |");
			ps.println("  -------------------------------");
			ps.println("4|   |   |   |   |   |   |   |   |");
			ps.println("  -------------------------------");
			ps.println("3|   |   |   |   |   |   |   |   |");
			ps.println("  -------------------------------");
			ps.println("2| P | P | P | P | P | P | P | P |");
			ps.println("  -------------------------------");
			ps.println("1| R | N | B | Q | K | B | N | R |");
			ps.println("  -------------------------------");
			ps.println("   a   b   c   d   e   f   g   h");
	    	ps.flush();
	    }	
	    
		//Actual
	    final ByteArrayOutputStream baosActual = new ByteArrayOutputStream();
	    try (PrintStream ps = new PrintStream(baosActual)) {
	    	ASCIIOutput output = new ASCIIOutput(ps);
	    	Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
	    	output.printBoard(board);
	    	ps.flush();
	    }    
	    
		assertEquals(new String(baosExp.toByteArray()), new String(baosActual.toByteArray()));
		
		//System.out.print(FENParser.parseFEN(FENParser.INITIAL_FEN).toString());
	}

}
