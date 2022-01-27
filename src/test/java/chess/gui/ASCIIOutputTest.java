package chess.gui;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import chess.gui.ASCIIOutput;
import chess.parsers.FENParser;


/**
 * @author Mauricio Coria
 *
 */
public class ASCIIOutputTest {

	private ASCIIOutput builder;
	private FENParser parser;

	@Before
	public void setUp() throws Exception {
	    builder = new ASCIIOutput();
	    parser = new FENParser(builder);  
	}
		
	@Test
	public void testGetPiecePlacement() {	    
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
	    parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
	    
	    final ByteArrayOutputStream baosActual = new ByteArrayOutputStream();
	    try (PrintStream ps = new PrintStream(baosActual)) {
	    	builder.getPiecePlacement(ps);
	    }    
	    
		assertEquals(new String(baosExp.toByteArray()), new String(baosActual.toByteArray()));

	}
	
	@Test
	public void testGetState() {	    
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
	    parser.parseFEN(FENParser.INITIAL_FEN);
	    
	    final ByteArrayOutputStream baosActual = new ByteArrayOutputStream();
	    try (PrintStream ps = new PrintStream(baosActual)) {
	    	builder.getState(ps);
	    }    
	    
		assertEquals(new String(baosExp.toByteArray()), new String(baosActual.toByteArray()));
	}	

}
