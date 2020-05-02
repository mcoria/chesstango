package chess;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import moveexecutors.CapturaPeonPromocion;
import moveexecutors.SaltoDoblePeonMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;

public class BoardTest01 {

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void test01() {
		Board tablero = builder.withFEN("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1").buildBoard();
		
		Collection<Move> moves = tablero.getLegalMoves();
		
		assertEquals(Color.NEGRO, tablero.getBoardState().getTurnoActual());
		assertTrue(tablero.isKingInCheck());
		assertEquals(1, moves.size());
	}

	
	@Test
	public void test02() {
		Board tablero = builder.withFEN("rnb1kbnr/pp1ppppp/8/q1p5/8/3P4/PPPKPPPP/RNBQ1BNR w KQkq - 0 1").buildBoard();
		
		assertEquals(Color.BLANCO, tablero.getBoardState().getTurnoActual());
		assertTrue(tablero.isKingInCheck());
		
		Collection<Move> moves = tablero.getLegalMoves();
		
		assertTrue(moves.contains( createSimpleMove(Square.b1, Pieza.CABALLO_BLANCO, Square.c3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.b2, Pieza.PEON_BLANCO, Square.b4, Square.b3) ));
		assertTrue(moves.contains( createSimpleMove(Square.c2, Pieza.PEON_BLANCO, Square.c3) ));
		assertTrue(moves.contains( createSimpleMove(Square.d2, Pieza.REY_BLANCO, Square.e3) ));
		
		assertFalse(moves.contains( createSimpleMove(Square.d2, Pieza.REY_BLANCO, Square.e1) ));
		
		
		assertEquals(4, tablero.getLegalMoves().size());
	}
	
	@Test
	public void testJuegoPeonPromocion() {
		Board board = builder.withFEN("r3k2r/p1ppqpb1/bn1Ppnp1/4N3/1p2P3/2N2Q2/PPPBBPpP/R4RK1 b kq - 0 2").buildBoard();
		
		Collection<Move> moves = board.getLegalMoves();
		
		assertTrue(moves.contains( createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO, Pieza.TORRE_NEGRO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO, Pieza.CABALLO_NEGRO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO, Pieza.REINA_NEGRO) ));
		
		assertEquals(46, board.getLegalMoves().size());
	}	
	
	
	@Test
	public void testJuegoPeonPasanteUndo() {
		Collection<Move> legalMoves = null;
		Move move = null;
		Board board = builder.withFEN("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2").buildBoard();
		
		//Estado inicial
		legalMoves = board.getLegalMoves();
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
		
		move = geteMove(legalMoves, Square.c7, Square.c5);
		board.execute(move);
		
		legalMoves = board.getLegalMoves();
		assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
		assertEquals(22, legalMoves.size());
		
		board.undo(move);
		
		legalMoves = board.getLegalMoves();
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
	}	
	
	private Move createSimpleMove(Square origenSquare, Pieza origenPieza, Square destinoSquare) {
		return new SimpleMove(new PosicionPieza(origenSquare, origenPieza), new PosicionPieza(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(Square origen, Pieza pieza, Square destinoSquare, Square squarePasante) {
		return new SaltoDoblePeonMove(new PosicionPieza(origen, pieza), new PosicionPieza(destinoSquare, null), squarePasante);
	}
	
	private Move createCapturePeonPromocion(Square origenSquare, Pieza origenPieza, Square destinoSquare, Pieza destinoPieza, Pieza promocion) {
		return new CapturaPeonPromocion(new PosicionPieza(origenSquare, origenPieza), new PosicionPieza(destinoSquare, destinoPieza), promocion);
	}
	
	
	protected boolean contieneMove(Collection<Move> movimientos, Square from, Square to) {
		for (Move move : movimientos) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return true;
			}
		}
		return false;
	}
	
	protected Move geteMove(Collection<Move> movimientos, Square from, Square to) {
		for (Move move : movimientos) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return move;
			}
		}
		return null;
	}	
}
