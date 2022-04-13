package chess.board.pseudomovesgenerators.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.builder.imp.PiecePlacementBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.PiecePlacement;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;
import chess.board.pseudomovesgenerators.strategies.PawnWhiteMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class PawnWhiteMoveGeneratorTest {
	private PawnWhiteMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 


	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moves = new ArrayList<Move>();
		
		moveGenerator = new PawnWhiteMoveGenerator();
		moveGenerator.setMoveFactory(moveFactoryImp);
	}
	
	@Test
	public void testSaltoSimple() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/P7/8/8");
		
		moveGenerator.setPiecePlacement(tablero);
		
		Square from = Square.a3;		
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a4) ));
	}
	
	@Test
	public void testSaltoDoble() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/8/P7/8");
		
		moveGenerator.setPiecePlacement(tablero);
		
		Square from = Square.a2;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a4, Square.a3) ));
	}
	
	@Test
	public void testSaltoDoble01() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/N7/P7/8");
		
		moveGenerator.setPiecePlacement(tablero);
		
		Square from = Square.a2;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(0, moves.size());
		

	}	
	
	@Test
	public void testAtaqueIzquierda() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/3p4/4P3/8");
		
		moveGenerator.setPiecePlacement(tablero);
		
		Square from = Square.e2;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.d3));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d3, Piece.PAWN_BLACK) ));
	}
	
	
	@Test
	public void testAtaqueDerecha() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/5p2/4P3/8");
		
		moveGenerator.setPiecePlacement(tablero);
		
		Square from = Square.e2;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.f3));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f3, Piece.PAWN_BLACK) ));
	}
	
	@Test
	public void testPawnSimplePawnPromocion() {
		PiecePlacement tablero = getTablero("8/3P4/8/8/8/8/8/8");
		
		moveGenerator.setPiecePlacement(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
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
		moveGenerator.setPiecePlacement(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.c8));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.d8));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.ROOK_WHITE) ));
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.KNIGHT_WHITE) ));
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.BISHOP_WHITE) ));
		assertTrue(moves.contains( createCapturePawnPromocion(origen, Square.c8, Piece.ROOK_BLACK, Piece.QUEEN_WHITE) ));
		
		assertEquals(4, moves.size());
	}

	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(PiecePositioned origen, Square destinoSquare, Square squarePasante) {
		return moveFactoryImp.createSaltoDoblePawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), squarePasante);
	}	
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza));
	}
	
	private Move createSimplePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece promocion) {
		return moveFactoryImp.createSimplePawnPromocion(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), promocion);
	}	
	
	private Move createCapturePawnPromocion(PiecePositioned origen, Square destinoSquare, Piece destinoPieza, Piece promocion) {
		return moveFactoryImp.createCapturePawnPromocion(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza), promocion);
	}	
	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}	
	
}
