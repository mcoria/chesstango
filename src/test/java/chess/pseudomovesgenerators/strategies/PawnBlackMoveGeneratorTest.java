package chess.pseudomovesgenerators.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.builder.imp.PiecePlacementBuilder;
import chess.debug.builder.DebugChessFactory;
import chess.fen.FENDecoder;
import chess.moves.Move;
import chess.moves.imp.MoveFactoryWhite;
import chess.position.PiecePlacement;
import chess.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public class PawnBlackMoveGeneratorTest {

	private PawnBlackMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moves = new ArrayList<Move>();
		
		moveGenerator = new PawnBlackMoveGenerator();
		moveGenerator.setMoveFactory(moveFactoryImp);
	}
	
	@Test
	public void testSaltoSimple() {
		PiecePlacement tablero =  getTablero("8/8/p7/8/8/8/8/8");

		moveGenerator.setTablero(tablero);
		
		Square from = Square.a6;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a5) ));
	}
	
	@Test
	public void testSaltoDoble() {		
		PiecePlacement tablero = getTablero("8/p7/8/8/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a7;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a5, Square.a6) ));
	}
	
	@Test
	public void testAtaqueIzquierda() {
		PiecePlacement tablero = getTablero("8/4p3/3P4/8/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e7;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.d6));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e5, Square.a6) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d6, Piece.PAWN_WHITE) ));
	}
	
	@Test
	public void testAtaqueDerecha() {
		PiecePlacement tablero = getTablero("8/4p3/5P2/8/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e7;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.f6));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e5, Square.e6) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f6, Piece.PAWN_WHITE) ));
		
		assertEquals(3, moves.size());
	}	
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleMove(origen, new PiecePositioned(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(PiecePositioned origen, Square destinoSquare, Square squarePasante) {
		return moveFactoryImp.createSaltoDoblePawnMove(origen, new PiecePositioned(destinoSquare, null), squarePasante);
	}	
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureMove(origen, new PiecePositioned(destinoSquare, destinoPieza));
	}
	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new DebugChessFactory());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}
}
