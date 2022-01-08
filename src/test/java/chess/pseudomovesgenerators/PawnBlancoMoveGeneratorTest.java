package chess.pseudomovesgenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.PosicionPiezaBoard;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.parsers.FENParser;
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.PawnBlancoMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class PawnBlancoMoveGeneratorTest {
	private PawnBlancoMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 


	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		
		moveGenerator = new PawnBlancoMoveGenerator();
		moveGenerator.setMoveFactory(moveFactory);
	}
	
	@Test
	public void testSaltoSimple() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/P7/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a3;		
		assertEquals(Pieza.PAWN_WHITE, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a4) ));
	}
	
	@Test
	public void testSaltoDoble() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/8/P7/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PAWN_WHITE, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a4, Square.a3) ));
	}
	
	@Test
	public void testSaltoDoble01() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/N7/P7/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PAWN_WHITE, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(0, moves.size());
		

	}	
	
	@Test
	public void testAtaqueIzquierda() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/3p4/4P3/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Pieza.PAWN_BLACK, tablero.getPieza(Square.d3));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d3, Pieza.PAWN_BLACK) ));
	}
	
	
	@Test
	public void testAtaqueDerecha() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/5p2/4P3/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Pieza.PAWN_BLACK, tablero.getPieza(Square.f3));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f3, Pieza.PAWN_BLACK) ));
	}
	
	@Test
	public void testPawnSimplePawnPromocion() {
		PosicionPiezaBoard tablero = getTablero("8/3P4/8/8/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Pieza.PAWN_WHITE, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimplePawnPromocion(origen, Square.d8, Pieza.ROOK_WHITE) ));
		assertTrue(moves.contains( createSimplePawnPromocion(origen, Square.d8, Pieza.KNIGHT_WHITE) ));
		assertTrue(moves.contains( createSimplePawnPromocion(origen, Square.d8, Pieza.BISHOP_WHITE) ));
		assertTrue(moves.contains( createSimplePawnPromocion(origen, Square.d8, Pieza.QUEEN_WHITE) ));
		
		assertEquals(4, moves.size());
	}

	@Test
	public void testPawnCapturaPawnPromocion() {
		PosicionPiezaBoard tablero = getTablero("2rr4/3P4/8/8/8/8/8/8");
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Pieza.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Pieza.ROOK_BLACK, tablero.getPieza(Square.c8));
		assertEquals(Pieza.ROOK_BLACK, tablero.getPieza(Square.d8));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Pieza.ROOK_BLACK, Pieza.ROOK_WHITE) ));
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Pieza.ROOK_BLACK, Pieza.KNIGHT_WHITE) ));
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Pieza.ROOK_BLACK, Pieza.BISHOP_WHITE) ));
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Pieza.ROOK_BLACK, Pieza.QUEEN_WHITE) ));
		
		assertEquals(4, moves.size());
	}

	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return moveFactory.createSimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(PosicionPieza origen, Square destinoSquare, Square squarePasante) {
		return moveFactory.createSaltoDoblePawnMove(origen, new PosicionPieza(destinoSquare, null), squarePasante);
	}	
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return moveFactory.createCaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}
	
	private Move createSimplePawnPromocion(PosicionPieza origen, Square destinoSquare, Pieza promocion) {
		return moveFactory.createSimplePawnPromocion(origen, new PosicionPieza(destinoSquare, null), promocion);
	}	
	
	private Move createCapturePawnPromocion(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza, Pieza promocion) {
		return moveFactory.createCapturePawnPromocion(origen, new PosicionPieza(destinoSquare, destinoPieza), promocion);
	}	
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
