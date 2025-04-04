package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.internal.moves.generators.pseudo.strategies.RookMoveGenerator;
import net.chesstango.board.internal.position.BitBoardDebug;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



/**
 * @author Mauricio Coria
 *
 */
public class RookMoveGeneratorTest {

	private RookMoveGenerator moveGenerator;
	
	private Collection<PseudoMove> moves;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moveGenerator = new RookMoveGenerator(Color.WHITE);
		moveGenerator.setMoveFactory(moveFactoryImp);
		moves = new ArrayList<>();
	}
	
	@Test
	public void testGetPseudoMoves01() {
		SquareBoard tablero =  getTablero("8/8/8/4R3/8/8/8/8");
		moveGenerator.setSquareBoard(tablero);

		BitBoard bitBoard = new BitBoardDebug();
		bitBoard.init(tablero);
		moveGenerator.setBitBoard(bitBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(from));
		
		PiecePositioned origen = PiecePositioned.of(from, Piece.ROOK_WHITE);
	
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(14, moves.size());
		
		//Norte
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e8) ));
		
		//Sur
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e1) ));
		
		//Este
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.a5) ));
		
		//Oeste
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.h5) ));		
	}
	
	
	@Test
	public void testGetPseudoMoves02() {		
		SquareBoard tablero =  getTablero("8/4p3/8/4R3/8/8/8/8");
		moveGenerator.setSquareBoard(tablero);


		BitBoard bitBoard = new BitBoardDebug();
		bitBoard.init(tablero);
		moveGenerator.setBitBoard(bitBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.e7));
		
		PiecePositioned origen = PiecePositioned.of(from, Piece.ROOK_WHITE);
	
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(13, moves.size());
		
		//Norte
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e7, Piece.PAWN_BLACK) ));
		
		//Sur
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e1) ));
		
		//Este
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.a5) ));
		
		//Oeste
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.h5) ));		
	}	

	private PseudoMove createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleKnightMove(origen, PiecePositioned.of(destinoSquare, null));
	}
	
	private PseudoMove createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureKnightMove(origen, PiecePositioned.of(destinoSquare, destinoPieza));
	}	
	
	private SquareBoard getTablero(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder();
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getChessRepresentation();
	}	
	
}
