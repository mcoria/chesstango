package chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.builder.ChessBuilderBoard;
import chess.debug.builder.DebugChessFactory;
import chess.moves.EnroqueBlancoReyMove;
import chess.moves.EnroqueBlancoReynaMove;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.parsers.FENParser;


/**
 * @author Mauricio Coria
 *
 */
public class BoardTest {
	
	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
	}	
	
	@Test
	public void test01() {		
		Board tablero =  getDefaultBoard();
		
		Collection<Move> moves = tablero.getLegalMoves();
		
		assertTrue(moves.contains( createSimpleMove(Square.a2, Pieza.PEON_BLANCO, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.a2, Pieza.PEON_BLANCO, Square.a4, Square.a3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.b2, Pieza.PEON_BLANCO, Square.b3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.b2, Pieza.PEON_BLANCO, Square.b4, Square.b3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.c2, Pieza.PEON_BLANCO, Square.c3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.c2, Pieza.PEON_BLANCO, Square.c4, Square.c3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.d2, Pieza.PEON_BLANCO, Square.d3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.d2, Pieza.PEON_BLANCO, Square.d4, Square.d3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.e2, Pieza.PEON_BLANCO, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.e2, Pieza.PEON_BLANCO, Square.e4, Square.e3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.f2, Pieza.PEON_BLANCO, Square.f3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.f2, Pieza.PEON_BLANCO, Square.f4, Square.f3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.g2, Pieza.PEON_BLANCO, Square.g3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.g2, Pieza.PEON_BLANCO, Square.g4, Square.g3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.h2, Pieza.PEON_BLANCO, Square.h3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.h2, Pieza.PEON_BLANCO, Square.h4, Square.h3) ));
		
		//Caballo Reyna
		assertTrue(moves.contains( createSimpleMove(Square.b1, Pieza.CABALLO_BLANCO, Square.a3) ));
		assertTrue(moves.contains( createSimpleMove(Square.b1, Pieza.CABALLO_BLANCO, Square.c3) ));
		
		//Caballo Rey
		assertTrue(moves.contains( createSimpleMove(Square.g1, Pieza.CABALLO_BLANCO, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(Square.g1, Pieza.CABALLO_BLANCO, Square.h3) ));
		
		//Debe haber 20 movimientos
		assertEquals(20, moves.size());
		
		//State
		assertEquals(Color.BLANCO, tablero.getBoardState().getTurnoActual());
		assertNull(tablero.getBoardState().getPeonPasanteSquare());
	}

	@Test
	public void testKingInCheck01() {
		Board tablero = getBoard("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

		BoardStatus result = tablero.getBoardStatus();
		Collection<Move> moves = tablero.getLegalMoves();

		assertEquals(Color.NEGRO, tablero.getBoardState().getTurnoActual());
		assertTrue(result.isKingInCheck());

		assertTrue(moves.contains(createCaptureMove(Square.h6, Pieza.CABALLO_NEGRO, Square.f7, Pieza.REINA_BLANCO)));
		assertFalse(moves.contains(createSimpleMove(Square.e8, Pieza.REY_NEGRO, Square.e7)));
		assertFalse(moves.contains(createCaptureMove(Square.e8, Pieza.REY_NEGRO, Square.f7, Pieza.REINA_BLANCO)));

		assertEquals(1, moves.size());
	}

	@Test
	public void testKingInCheck02() {
		Board tablero = getBoard("rnb1kbnr/pp1ppppp/8/q1p5/8/3P4/PPPKPPPP/RNBQ1BNR w KQkq - 0 1");

		BoardStatus result = tablero.getBoardStatus();

		assertEquals(Color.BLANCO, tablero.getBoardState().getTurnoActual());
		assertTrue(result.isKingInCheck());

		Collection<Move> moves = tablero.getLegalMoves();

		assertTrue(moves.contains(createSimpleMove(Square.b1, Pieza.CABALLO_BLANCO, Square.c3)));
		assertTrue(moves.contains(createSaltoDobleMove(Square.b2, Pieza.PEON_BLANCO, Square.b4, Square.b3)));
		assertTrue(moves.contains(createSimpleMove(Square.c2, Pieza.PEON_BLANCO, Square.c3)));
		assertTrue(moves.contains(createSimpleMove(Square.d2, Pieza.REY_BLANCO, Square.e3)));

		assertFalse(moves.contains(createSimpleMove(Square.d2, Pieza.REY_BLANCO, Square.e1)));

		assertEquals(4, moves.size());
	}

	@Test
	public void testJuegoEnroqueBlancoJaque() {		
		Board tablero = getBoard("r3k3/8/8/8/4r3/8/8/R3K2R w KQq - 0 1");

		BoardStatus result = tablero.getBoardStatus();

		assertEquals(Color.BLANCO, tablero.getBoardState().getTurnoActual());
		assertTrue(result.isKingInCheck());

		Collection<Move> moves = tablero.getLegalMoves();

		assertTrue(moves.contains(createSimpleReyMoveBlanco(Square.e1, Square.d1)));
		assertTrue(moves.contains(createSimpleReyMoveBlanco(Square.e1, Square.d2)));
		assertFalse(moves.contains(createSimpleReyMoveBlanco(Square.e1, Square.e2)));
		assertTrue(moves.contains(createSimpleReyMoveBlanco(Square.e1, Square.f2)));
		assertTrue(moves.contains(createSimpleReyMoveBlanco(Square.e1, Square.f1)));

		assertFalse(moves.contains(new EnroqueBlancoReyMove()));
		assertFalse(moves.contains(new EnroqueBlancoReynaMove()));

		assertEquals(4, moves.size());
	}

	@Test
	public void testJuegoPeonPromocion() {
		Board tablero = getBoard("r3k2r/p1ppqpb1/bn1Ppnp1/4N3/1p2P3/2N2Q2/PPPBBPpP/R4RK1 b kq - 0 2");

		Collection<Move> moves = tablero.getLegalMoves();

		assertTrue(moves.contains(createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO,
				Pieza.TORRE_NEGRO)));
		assertTrue(moves.contains(createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO,
				Pieza.CABALLO_NEGRO)));
		assertTrue(moves.contains(createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO,
				Pieza.ALFIL_NEGRO)));
		assertTrue(moves.contains(createCapturePeonPromocion(Square.g2, Pieza.PEON_NEGRO, Square.f1, Pieza.TORRE_BLANCO,
				Pieza.REINA_NEGRO)));

		assertEquals(46, moves.size());
	}

	@Test
	public void testJuegoPeonPasanteUndo() {
		Collection<Move> legalMoves = null;
		Move move = null;

		Board tablero = getBoard("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2");

		// Estado inicial
		legalMoves = tablero.getLegalMoves();
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));

		// Mueve el peon pasante
		move = geteMove(legalMoves, Square.c7, Square.c5);
		tablero.execute(move);

		// Podemos capturarlo
		legalMoves = tablero.getLegalMoves();
		assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
		assertEquals(22, legalMoves.size());

		// Volvemos atras
		tablero.undo(move);

		// No podemos capturarlo
		legalMoves = tablero.getLegalMoves();
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
	}

	@Test
	public void testJuegoPeonPasante01() {
		Collection<Move> legalMoves = null;
		Move move = null;
		Board tablero = getBoard("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2");

		// Estado inicial
		legalMoves = tablero.getLegalMoves();
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));

		// Mueve el peon pasante
		move = geteMove(legalMoves, Square.c7, Square.c5);
		tablero.execute(move);

		// Podemos capturarlo
		legalMoves = tablero.getLegalMoves();
		assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
		assertEquals(22, legalMoves.size());

		// Pero NO lo capturamos
		legalMoves = tablero.getLegalMoves();
		move = geteMove(legalMoves, Square.h2, Square.h3);
		tablero.execute(move);

		legalMoves = tablero.getLegalMoves();
		move = geteMove(legalMoves, Square.h7, Square.h6);
		tablero.execute(move);

		// Ahora no podemos capturar el peon pasante !!!
		legalMoves = tablero.getLegalMoves();
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
	}
	
	
	@Test
	public void testJauqeMate() {	
		Board tablero = getBoard("r1bqk1nr/pppp1Qpp/2n5/2b1p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");
		
		BoardStatus status = tablero.getBoardStatus();
		
		assertTrue(status.isKingInCheck());
		assertFalse(status.isExistsLegalMove());
	
	}
	
	@Test
	public void testReyNoPuedeMoverAJaque(){
		Board tablero = getBoard("8/8/8/8/8/8/6k1/4K2R w K - 0 1");
		
		Collection<Move> moves = tablero.getLegalMoves();
		
		assertFalse(moves.contains(createSimpleMove(Square.e1, Pieza.REY_BLANCO, Square.f2)));
		assertFalse(moves.contains(createSimpleMove(Square.e1, Pieza.REY_BLANCO, Square.f1)));
		assertFalse(moves.contains(new EnroqueBlancoReyMove()));
		
		assertEquals(12, moves.size());
		
	}
	
	@Test
	public void testMovimientoPeonPasanteNoPermitido(){
		Board tablero = getBoard("8/2p5/3p4/KP5r/1R3pPk/8/4P3/8 b - g3 0 1");
		
		Collection<Move> moves = tablero.getLegalMoves();
		
		assertFalse(moves.contains(createCapturePeonPasanteMoveNegro(Square.f4, Square.g3)));
		
		assertEquals(17, moves.size());
		
	}

	protected boolean contieneMove(Collection<Move> movimientos, Square from, Square to) {
		for (Move move : movimientos) {
			if (from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())) {
				return true;
			}
		}
		return false;
	}

	protected Move geteMove(Collection<Move> movimientos, Square from, Square to) {
		for (Move move : movimientos) {
			if (from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())) {
				return move;
			}
		}
		return null;
	}
		
	
	private Move createSimpleMove(Square origenSquare, Pieza origenPieza, Square destinoSquare) {
		return moveFactory.createSimpleMove(new PosicionPieza(origenSquare, origenPieza), new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(Square origenSquare, Pieza origenPieza, Square destinoSquare, Pieza destinoPieza) {
		return moveFactory.createCaptureMove(new PosicionPieza(origenSquare, origenPieza), new PosicionPieza(destinoSquare, destinoPieza));
	}		

	private Move createSaltoDobleMove(Square origen, Pieza pieza, Square destinoSquare, Square squarePasante) {
		return moveFactory.createSaltoDoblePeonMove(new PosicionPieza(origen, pieza), new PosicionPieza(destinoSquare, null),  squarePasante);		
	}

	private Move createCapturePeonPromocion(Square origenSquare, Pieza origenPieza, Square destinoSquare,
			Pieza destinoPieza, Pieza promocion) {
		return moveFactory.createCapturePeonPromocion(new PosicionPieza(origenSquare, origenPieza),
				new PosicionPieza(destinoSquare, destinoPieza), promocion);
	}
	
	private Move createCapturePeonPasanteMoveNegro(Square origen, Square destinoSquare) {
		return moveFactory.createCapturePeonPasante(new PosicionPieza(origen, Pieza.PEON_NEGRO),
				new PosicionPieza(destinoSquare, null), new PosicionPieza(
						Square.getSquare(destinoSquare.getFile(), destinoSquare.getRank() + 1), Pieza.PEON_BLANCO));
	}
	
	private Object createSimpleReyMoveBlanco(Square origen, Square destino) {
		return moveFactory.createSimpleReyMoveBlanco(new PosicionPieza(origen, Pieza.REY_BLANCO), new PosicionPieza(destino, null));
	}	
	
	private Board getDefaultBoard() {
		return getBoard(FENParser.INITIAL_FEN);
	}	
	
	private Board getBoard(String string) {		
		ChessBuilderBoard builder = new ChessBuilderBoard(new DebugChessFactory());

		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(string);
		
		return builder.getBoard();
	}		
		
}
