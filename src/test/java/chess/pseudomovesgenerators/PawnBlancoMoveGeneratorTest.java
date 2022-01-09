package chess.pseudomovesgenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.parsers.FENParser;
import chess.position.PiecePlacement;
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
		PiecePlacement tablero =  getTablero("8/8/8/8/8/P7/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a3;		
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a4) ));
	}
	
	@Test
	public void testSaltoDoble() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/8/P7/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a4, Square.a3) ));
	}
	
	@Test
	public void testSaltoDoble01() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/N7/P7/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(0, moves.size());
		

	}	
	
	@Test
	public void testAtaqueIzquierda() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/3p4/4P3/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.d3));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d3, Piece.PAWN_BLACK) ));
	}
	
	
	@Test
	public void testAtaqueDerecha() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/5p2/4P3/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.f3));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f3, Piece.PAWN_BLACK) ));
	}
	
	@Test
	public void testPawnSimplePawnPromocion() {
		PiecePlacement tablero = getTablero("8/3P4/8/8/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimplePawnPromocion(origen, Square.d8, Piece.ROOK_WHITE) ));
		assertTrue(moves.contains( createSimplePawnPromocion(origen, Square.d8, Piece.KNIGHT_WHITE) ));
		assertTrue(moves.contains( createSimplePawnPromocion(origen, Square.d8, Piece.BISHOP_WHITE) ));
		assertTrue(moves.contains( createSimplePawnPromocion(origen, Square.d8, Piece.QUEEN_WHITE) ));
		
		assertEquals(4, moves.size());
	}

	@Test
	public void testPawnCapturaPawnPromocion() {
		PiecePlacement tablero = getTablero("2rr4/3P4/8/8/8/8/8/8");
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.c8));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.d8));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.ROOK_WHITE) ));
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.KNIGHT_WHITE) ));
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.BISHOP_WHITE) ));
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.QUEEN_WHITE) ));
		
		assertEquals(4, moves.size());
	}

	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactory.createSimpleMove(origen, new PiecePositioned(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(PiecePositioned origen, Square destinoSquare, Square squarePasante) {
		return moveFactory.createSaltoDoblePawnMove(origen, new PiecePositioned(destinoSquare, null), squarePasante);
	}	
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactory.createCaptureMove(origen, new PiecePositioned(destinoSquare, destinoPieza));
	}
	
	private Move createSimplePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece promocion) {
		return moveFactory.createSimplePawnPromocion(origen, new PiecePositioned(destinoSquare, null), promocion);
	}	
	
	private Move createCapturePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Piece promocion) {
		return moveFactory.createCapturePawnPromocion(origen, new PiecePositioned(destinoSquare, destinoPieza), promocion);
	}	
	
	private PiecePlacement getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
