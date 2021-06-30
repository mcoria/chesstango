package chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import builder.ChessFactory;
import debug.builder.DebugChessFactory;
import moveexecutors.CapturaPeonPromocion;
import moveexecutors.CaptureMove;
import moveexecutors.CaptureReyMove;
import moveexecutors.EnroqueBlancoReyMove;
import moveexecutors.EnroqueBlancoReynaMove;
import moveexecutors.SaltoDoblePeonMove;
import moveexecutors.SimpleMove;
import moveexecutors.SimpleReyMove;
import parsers.FENBoarBuilder;

public class BoardTest01 {

	private FENBoarBuilder builder;
	private ChessFactory debugChessFactory;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		debugChessFactory = new DebugChessFactory();
	}
	
	@Test
	public void test01() {
		Board tablero = builder.withFEN("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1").withChessFactory(debugChessFactory).buildBoard();
		
		BoardStatus result = tablero.getBoardStatus();
		Collection<Move> moves = result.getLegalMoves();
		
		assertEquals(Color.NEGRO, tablero.getBoardState().getTurnoActual());
		assertTrue(result.isKingInCheck());		
		
		assertTrue(moves.contains( createCaptureMove(Square.h6, Pieza.CABALLO_NEGRO, Square.f7, Pieza.REINA_BLANCO) ));
		assertFalse(moves.contains( createSimpleMove(Square.e8, Pieza.REY_NEGRO, Square.e7) ));
		assertFalse(moves.contains( createCaptureMove(Square.e8, Pieza.REY_NEGRO, Square.f7, Pieza.REINA_BLANCO) ));

		assertEquals(1, moves.size());
	}

	@Test
	public void test02() {
		Board tablero = builder.withFEN("rnb1kbnr/pp1ppppp/8/q1p5/8/3P4/PPPKPPPP/RNBQ1BNR w KQkq - 0 1").withChessFactory(debugChessFactory).buildBoard();
		
		BoardStatus result = tablero.getBoardStatus();
		
		assertEquals(Color.BLANCO, tablero.getBoardState().getTurnoActual());
		assertTrue(result.isKingInCheck());
		
		Collection<Move> moves = result.getLegalMoves(); 
		
		assertTrue(moves.contains( createSimpleMove(Square.b1, Pieza.CABALLO_BLANCO, Square.c3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.b2, Pieza.PEON_BLANCO, Square.b4, Square.b3) ));
		assertTrue(moves.contains( createSimpleMove(Square.c2, Pieza.PEON_BLANCO, Square.c3) ));
		assertTrue(moves.contains( createSimpleMove(Square.d2, Pieza.REY_BLANCO, Square.e3) ));
		
		assertFalse(moves.contains( createSimpleMove(Square.d2, Pieza.REY_BLANCO, Square.e1) ));
		
		
		assertEquals(4, result.getLegalMoves().size());
	}
	
	@Test
	public void testJuegoEnroqueBlancoJaque() {
		Board tablero = builder.withFEN("r3k3/8/8/8/4r3/8/8/R3K2R w KQq - 0 1").buildBoard();
		
		BoardStatus result = tablero.getBoardStatus();
		
		assertEquals(Color.BLANCO, tablero.getBoardState().getTurnoActual());
		assertTrue(result.isKingInCheck());			
		
		Collection<Move> moves = result.getLegalMoves();
		
		assertTrue(moves.contains( createSimpleMove(Square.e1, Pieza.REY_BLANCO, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(Square.e1, Pieza.REY_BLANCO, Square.d2) ));
		assertFalse(moves.contains( createSimpleMove(Square.e1, Pieza.REY_BLANCO, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(Square.e1, Pieza.REY_BLANCO, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(Square.e1, Pieza.REY_BLANCO, Square.f1) ));
		
		assertFalse(moves.contains( new EnroqueBlancoReyMove() ));
		assertFalse(moves.contains( new EnroqueBlancoReynaMove() ));
		
		assertEquals(4, moves.size());		
	}	
	
	@Test
	public void testJuegoPeonPromocion() {
		Board tablero = builder.withFEN("r3k2r/p1ppqpb1/bn1Ppnp1/4N3/1p2P3/2N2Q2/PPPBBPpP/R4RK1 b kq - 0 2").buildBoard();
		
		BoardStatus result = tablero.getBoardStatus();
		
		Collection<Move> moves = result.getLegalMoves();
		
		assertTrue(moves.contains( createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO, Pieza.TORRE_NEGRO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO, Pieza.CABALLO_NEGRO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO, Pieza.REINA_NEGRO) ));
		
		assertEquals(46, result.getLegalMoves().size());
	}	
	
	
	@Test
	public void testJuegoPeonPasanteUndo() {
		Collection<Move> legalMoves = null;
		Move move = null;
		BoardStatus result = null;
		Board tablero = builder.withFEN("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2").buildBoard();
		
		
		
		//Estado inicial
		result = tablero.getBoardStatus();
		legalMoves = result.getLegalMoves();
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
		
		//Mueve el peon pasante
		move = geteMove(legalMoves, Square.c7, Square.c5);
		tablero.execute(move);
		
		//Podemos capturarlo
		result = tablero.getBoardStatus();
		legalMoves = result.getLegalMoves();
		assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
		assertEquals(22, legalMoves.size());
		
		//Volvemos atras
		tablero.undo(move);
		
		//No podemos capturarlo
		result = tablero.getBoardStatus();
		legalMoves = result.getLegalMoves();
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
	}
	
	
	@Test
	public void testJuegoPeonPasante01() {
		Collection<Move> legalMoves = null;
		BoardStatus result = null;
		Move move = null;
		Board tablero = builder.withFEN("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2").buildBoard();
		
		//Estado inicial
		result = tablero.getBoardStatus();
		legalMoves = result.getLegalMoves();
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
		
		//Mueve el peon pasante
		move = geteMove(legalMoves, Square.c7, Square.c5);
		tablero.execute(move);
		
		//Podemos capturarlo
		result = tablero.getBoardStatus();
		legalMoves = result.getLegalMoves();
		assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
		assertEquals(22, legalMoves.size());

		//Pero NO lo capturamos
		result = tablero.getBoardStatus();
		legalMoves = result.getLegalMoves();
		move = geteMove(legalMoves, Square.h2, Square.h3);
		tablero.execute(move);
		

		result = tablero.getBoardStatus();
		legalMoves = result.getLegalMoves();
		move = geteMove(legalMoves, Square.h7, Square.h6);
		tablero.execute(move);
		
		//Ahora no podemos capturar el peon pasante !!!
		result = tablero.getBoardStatus();
		legalMoves = result.getLegalMoves();
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
	}
	
	
	private Move createSimpleMove(Square origenSquare, Pieza origenPieza, Square destinoSquare) {
		if(Pieza.REY_NEGRO.equals(origenPieza) || Pieza.REY_BLANCO.equals(origenPieza) ){
			return new SimpleReyMove(new PosicionPieza(origenSquare, origenPieza), new PosicionPieza(destinoSquare, null));
		}		
		return new SimpleMove(new PosicionPieza(origenSquare, origenPieza), new PosicionPieza(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(Square origen, Pieza pieza, Square destinoSquare, Square squarePasante) {
		return new SaltoDoblePeonMove(new PosicionPieza(origen, pieza), new PosicionPieza(destinoSquare, null), squarePasante);
	}
	
	private Move createCapturePeonPromocion(Square origenSquare, Pieza origenPieza, Square destinoSquare, Pieza destinoPieza, Pieza promocion) {
		return new CapturaPeonPromocion(new PosicionPieza(origenSquare, origenPieza), new PosicionPieza(destinoSquare, destinoPieza), promocion);
	}
	
	
	private Move createCaptureMove(Square origenSquare, Pieza origenPieza, Square destinoSquare, Pieza destinoPieza) {
		if(Pieza.REY_NEGRO.equals(origenPieza) || Pieza.REY_BLANCO.equals(origenPieza) ){
			return new CaptureReyMove(new PosicionPieza(origenSquare, origenPieza), new PosicionPieza(destinoSquare, destinoPieza));
		}
		return new CaptureMove(new PosicionPieza(origenSquare, origenPieza), new PosicionPieza(destinoSquare, destinoPieza));
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
