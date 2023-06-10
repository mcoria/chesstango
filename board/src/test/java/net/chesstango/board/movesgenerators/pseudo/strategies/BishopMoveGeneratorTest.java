package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.PiecePlacementBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.ColorBoard;
import net.chesstango.board.position.imp.ColorBoardImp;
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
public class BishopMoveGeneratorTest {
	
	private BishopMoveGenerator moveGenerator = null;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryWhite();
		
		moveGenerator = new BishopMoveGenerator(Color.WHITE);
		moveGenerator.setMoveFactory(moveFactoryImp);
	}
	
	@Test
	public void testGetPseudoMoves01() {
		Board tablero =  getTablero("8/8/8/4B3/8/8/8/8");
		
		moveGenerator.setBoard(tablero);

		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);

		Square from = Square.e5;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(from));
		
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
		Board tablero =  getTablero("8/8/8/6p1/8/8/PPP1PPPP/2B5");
		
		moveGenerator.setBoard(tablero);

		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);

		Square from = Square.c1;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.g5));
		
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
				affectedBySquares.add(Square.getSquareByIdx(i));
			}			
		}
		return affectedBySquares;
	}
	
	private Board getTablero(String string) {
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getChessRepresentation();
	}	
}
