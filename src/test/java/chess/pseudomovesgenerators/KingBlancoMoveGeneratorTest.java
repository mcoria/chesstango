package chess.pseudomovesgenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.CachePosiciones;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.ColorBoard;
import chess.layers.PosicionPiezaBoard;
import chess.moves.EnroqueBlancoKingMove;
import chess.moves.EnroqueBlancoQueenMove;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class KingBlancoMoveGeneratorTest {
	private KingBlancoMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private BoardState state;

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		state = new BoardState();
		
		moveGenerator = new KingBlancoMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactory);
	}
	
	@Test
	public void test01() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/4K3/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		
		assertEquals(8, moves.size());
	}

	@Test
	public void test02() {
		PosicionPiezaBoard tablero = getTablero("8/8/4P3/4K3/4p3/8/8/8");
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e6));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e4));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e4, Pieza.PEON_NEGRO) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		
		assertEquals(7, moves.size());
	}
	
	
	@Test
	public void testEnroqueBlancoReina01() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/8/8/R3K3");
		
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));

		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoQueenMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testEnroqueBlancoReina02() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/5b2/8/R3K3");
		
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		

		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f3));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoQueenMove() ));
		
		assertEquals(6, moves.size());		
	}
	
	@Test
	public void testEnroqueBlancoReina03() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/5b2/8/8/R3K3");
		
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
	
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f4));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoQueenMove() ));
		
		assertEquals(6, moves.size());	
	}
	
	@Test
	public void testEnroqueBlancoReina04() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/8/8/RN2K3");
		
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(Square.b1));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( new EnroqueBlancoQueenMove() ));
		
		assertEquals(5, moves.size());	
	}	
	
	@Test
	public void testEnroqueBlancoKing01() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/8/8/4K2R");
		
		state.setEnroqueBlancoKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoKingMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testEnroqueBlancoKing02() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/3b4/8/4K2R");
		
		state.setEnroqueBlancoKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));

		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d3));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoKingMove() ));
		
		assertEquals(6, moves.size());		
	}
	
	@Test
	public void testEnroqueBlancoKing03() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/3b4/8/8/4K2R");
		
		state.setEnroqueBlancoKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d4));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoKingMove() ));
		
		assertEquals(6, moves.size());
	}		
	
	@Test
	public void testEnroqueBlancoKing04() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/8/6p1/4K2R");
		
		state.setEnroqueBlancoKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g2));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoKingMove() ));
		
		assertEquals(6, moves.size());
	}		

	@Test
	public void testEnroqueBlancoJaque() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/4r3/8/8/R3K2R");
		
		state.setEnroqueBlancoKingPermitido(true);
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.e4));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertTrue(moves.contains( new EnroqueBlancoKingMove() ));
		assertTrue(moves.contains( new EnroqueBlancoQueenMove() ));
		
		assertEquals(7, moves.size());		
	}
	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return moveFactory.createSimpleKingMoveBlanco(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return moveFactory.createCaptureKingMoveBlanco(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
