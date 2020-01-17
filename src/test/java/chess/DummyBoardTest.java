package chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Set;

import org.junit.Test;

import chess.Move.MoveType;
import parsers.FENParser;

public class DummyBoardTest {

	@Test
	public void test01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		Set<Move> moves = tablero.getPseudoMoves(Color.BLANCO);
		
		assertEquals(20, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(Square.a2, Pieza.PEON_BLANCO, Square.a3) ));
		assertTrue(moves.contains( createSimpleMove(Square.a2, Pieza.PEON_BLANCO, Square.a4) ));
		assertTrue(moves.contains( createSimpleMove(Square.b2, Pieza.PEON_BLANCO, Square.b3) ));
		assertTrue(moves.contains( createSimpleMove(Square.b2, Pieza.PEON_BLANCO, Square.b4) ));
		assertTrue(moves.contains( createSimpleMove(Square.c2, Pieza.PEON_BLANCO, Square.c3) ));
		assertTrue(moves.contains( createSimpleMove(Square.c2, Pieza.PEON_BLANCO, Square.c4) ));
		assertTrue(moves.contains( createSimpleMove(Square.d2, Pieza.PEON_BLANCO, Square.d3) ));
		assertTrue(moves.contains( createSimpleMove(Square.d2, Pieza.PEON_BLANCO, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(Square.e2, Pieza.PEON_BLANCO, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(Square.e2, Pieza.PEON_BLANCO, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(Square.f2, Pieza.PEON_BLANCO, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(Square.f2, Pieza.PEON_BLANCO, Square.f4) ));
		assertTrue(moves.contains( createSimpleMove(Square.g2, Pieza.PEON_BLANCO, Square.g3) ));
		assertTrue(moves.contains( createSimpleMove(Square.g2, Pieza.PEON_BLANCO, Square.g4) ));
		assertTrue(moves.contains( createSimpleMove(Square.h2, Pieza.PEON_BLANCO, Square.h3) ));
		assertTrue(moves.contains( createSimpleMove(Square.h2, Pieza.PEON_BLANCO, Square.h4) ));
		
		//Caballo Reyna
		assertTrue(moves.contains( createSimpleMove(Square.b1, Pieza.CABALLO_BLANCO, Square.a3) ));
		assertTrue(moves.contains( createSimpleMove(Square.b1, Pieza.CABALLO_BLANCO, Square.c3) ));
		
		//Caballo Rey
		assertTrue(moves.contains( createSimpleMove(Square.g1, Pieza.CABALLO_BLANCO, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(Square.g1, Pieza.CABALLO_BLANCO, Square.h3) ));
	}
	
	private Move createSimpleMove(Square origenSquare, Pieza origenPieza, Square destinoSquare) {
		return new Move(new SimpleImmutableEntry<Square, Pieza>(origenSquare, origenPieza), new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SIMPLE);
	}	
		
}
