package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.Move.MoveType;
import chess.Pieza;
import chess.Square;
import parsers.FENParser;

public class ReyBlancoMoveGeneratorTest {

	@Test
	public void test01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4K3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
	
		ReyBlancoMoveGenerator moveGenerator = new ReyBlancoMoveGenerator();
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, origen);
		
		assertEquals(8, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));			
	}

	@Test
	public void test02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/4P3/4K3/4p3/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e6));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e4));
	
		ReyBlancoMoveGenerator moveGenerator = new ReyBlancoMoveGenerator();
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, origen);
		
		assertEquals(7, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e4, Pieza.PEON_NEGRO) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));			
	}
	
	
	@Test
	public void testEnroqueReyBlancoReina01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/8/8/R3K3");
		
		Square from = Square.e1;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReinaPermitido(true);	
		
		ReyBlancoMoveGenerator moveGenerator = new ReyBlancoMoveGenerator();
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, boardState, origen);
		
		assertEquals(6, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( Move.ENROQUE_BLANCO_REYNA ));
	}
	
	@Test
	public void testEnroqueReyBlancoReina02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/5b2/8/R3K3");
		
		Square from = Square.e1;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f3));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReinaPermitido(true);	
		
		ReyBlancoMoveGenerator moveGenerator = new ReyBlancoMoveGenerator();
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, boardState, origen);
		
		assertEquals(5, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
	}
	
	@Test
	public void testEnroqueReyBlancoReina03() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/5b2/8/8/R3K3");
		
		Square from = Square.e1;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f4));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReinaPermitido(true);	
		
		ReyBlancoMoveGenerator moveGenerator = new ReyBlancoMoveGenerator();
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Set<Move> moves = moveGenerator.getLegalMoves(tablero, boardState, origen);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
	}		
	
	@Test
	public void testEnroqueReyBlancoRey01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/8/8/4K2R");
		
		Square from = Square.e1;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReyPermitido(true);	
		
		ReyBlancoMoveGenerator moveGenerator = new ReyBlancoMoveGenerator();
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, boardState, origen);
		
		assertEquals(6, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( Move.ENROQUE_BLANCO_REY ));
	}	
	
	@Test
	public void testEnroqueReyBlancoRey02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/3b4/8/4K2R");
		
		Square from = Square.e1;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d3));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReyPermitido(true);	
		
		ReyBlancoMoveGenerator moveGenerator = new ReyBlancoMoveGenerator();
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, boardState, origen);
		
		assertEquals(5, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
	}
	
	@Test
	public void testEnroqueReyBlancoRey03() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/3b4/8/8/4K2R");
		
		Square from = Square.e1;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d4));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReyPermitido(true);	
		
		ReyBlancoMoveGenerator moveGenerator = new ReyBlancoMoveGenerator();
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Set<Move> moves = moveGenerator.getLegalMoves(tablero, boardState, origen);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
	}		

	@Test
	public void testEnroqueReyBlancoJaque() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/4r3/8/8/R3K2R");
		
		Square from = Square.e1;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.e4));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReinaPermitido(true);	
		
		ReyBlancoMoveGenerator moveGenerator = new ReyBlancoMoveGenerator();
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, boardState, origen);
		
		assertEquals(5, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
	}
	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SIMPLE);
	}
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza), MoveType.CAPTURA);
	}	
	
}
