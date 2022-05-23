package chess.board.pseudomovesgenerators.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.builder.imp.PiecePlacementBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.debug.chess.ColorBoardDebug;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public class BishopMoveGeneratorTest {
	
	private BishopMoveGenerator moveGenerator = null;

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		
		moveGenerator = new BishopMoveGenerator(Color.WHITE);
		moveGenerator.setMoveFactory(moveFactoryImp);
	}
	
	@Test
	public void testGetPseudoMoves01() {
		PiecePlacement tablero =  getTablero("8/8/8/4B3/8/8/8/8");
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);

		Square from = Square.e5;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.BISHOP_WHITE);

		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		Collection<Move> moves = generatorResult.getPseudoMoves();
		
		// NorteEste
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.h8) ));

		// SurEste
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.h2) ));

		// SurOeste
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.a1) ));

		// NorteOeste
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b8) ));
		
		assertEquals(13, moves.size());

		
		Collection<Square> affectedBySquares = toSquareCollection(generatorResult.getAffectedByPositions());
		
		// NorteEste
		assertTrue(affectedBySquares.contains( Square.f6 ));
		assertTrue(affectedBySquares.contains( Square.g7 ));
		assertTrue(affectedBySquares.contains( Square.h8 ));
		
		// SurEste
		assertTrue(affectedBySquares.contains( Square.f4 ));
		assertTrue(affectedBySquares.contains( Square.g3 ));
		assertTrue(affectedBySquares.contains( Square.h2 ));
		
		// SurOeste
		assertTrue(affectedBySquares.contains( Square.d4 ));
		assertTrue(affectedBySquares.contains( Square.c3 ));
		assertTrue(affectedBySquares.contains( Square.b2 ));
		assertTrue(affectedBySquares.contains( Square.a1 ));
		
		// NorteOeste
		assertTrue(affectedBySquares.contains( Square.d6 ));
		assertTrue(affectedBySquares.contains( Square.c7 ));
		assertTrue(affectedBySquares.contains( Square.b8 ));
		
		assertEquals(13, affectedBySquares.size());
		
	}

	@Test
	public void testGetPseudoMoves02() {
		PiecePlacement tablero =  getTablero("8/8/8/6p1/8/8/PPP1PPPP/2B5");
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);

		Square from = Square.c1;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.g5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.BISHOP_WHITE);

		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		Collection<Move> moves = generatorResult.getPseudoMoves();

		//Moves
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.g5, Piece.PAWN_BLACK) ));
		
		assertEquals(4, moves.size());

		Collection<Square> affectedBySquares =  toSquareCollection(generatorResult.getAffectedByPositions());

		//affectedBySquares
		assertTrue(affectedBySquares.contains( Square.d2 ));
		assertTrue(affectedBySquares.contains( Square.e3 ));
		assertTrue(affectedBySquares.contains( Square.f4 ));
		assertTrue(affectedBySquares.contains( Square.g5 ));
		assertTrue(affectedBySquares.contains( Square.b2 ));
		
		assertEquals(5, affectedBySquares.size());		

	}	

	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
	}
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza));
	}
	
	private Collection<Square> toSquareCollection(long affectedBy) {
		Collection<Square> affectedBySquares = new ArrayList<Square>();
		for(int i = 0; i < 64; i++){
			if( (affectedBy & (1L << i))  != 0 ) {
				affectedBySquares.add(Square.getSquare(i));
			}			
		}
		return affectedBySquares;
	}
	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}	
}
