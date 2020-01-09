package movegenerators;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import chess.Move.MoveType;
import parsers.FENParser;

public class ReyMoveGeneratorTest {

	@Test
	public void testNorte() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4K3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
	
		ReyMoveGenerator moveGenerator = new ReyMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, from);
		
		assertEquals(8, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.e6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.e6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.d5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.f5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.e4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.e4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.e4, MoveType.SIMPLE)));			
	}

}
