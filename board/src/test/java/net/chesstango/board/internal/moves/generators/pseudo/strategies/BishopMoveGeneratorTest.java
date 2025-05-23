package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.internal.position.BitBoardDebug;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.BitBoard;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENExporter;
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
		moveFactoryImp = new MoveFactoryWhite();
		
		moveGenerator = new BishopMoveGenerator(Color.WHITE);
		moveGenerator.setMoveFactory(moveFactoryImp);
	}
	
	@Test
	public void testGetPseudoMoves01() {
		SquareBoard tablero =  getTablero("8/8/8/4B3/8/8/8/8 w KQkq - 0 1");
		
		moveGenerator.setSquareBoard(tablero);

		BitBoard bitBoard = new BitBoardDebug();
		bitBoard.init(tablero);
		moveGenerator.setBitBoard(bitBoard);

		Square from = Square.e5;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(from));
		
		PiecePositioned origen = PiecePositioned.of(from, Piece.BISHOP_WHITE);

		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

		MoveList<PseudoMove> moves = generatorResult.getPseudoMoves();
		
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

		// La posicion del Bishop
		assertTrue(affectedBySquares.contains( Square.e5 ));
		
		assertEquals(14, affectedBySquares.size());
		
	}

	@Test
	public void testGetPseudoMoves02() {
		SquareBoard tablero =  getTablero("8/8/8/6p1/8/8/PPP1PPPP/2B5 w KQkq - 0 1");
		
		moveGenerator.setSquareBoard(tablero);

		BitBoard bitBoard = new BitBoardDebug();
		bitBoard.init(tablero);
		moveGenerator.setBitBoard(bitBoard);

		Square from = Square.c1;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.g5));
		
		PiecePositioned origen = PiecePositioned.of(from, Piece.BISHOP_WHITE);

		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);

		MoveList<PseudoMove> moves = generatorResult.getPseudoMoves();

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

		// La posicion del Bishop
		assertTrue(affectedBySquares.contains( Square.c1 ));
		
		assertEquals(6, affectedBySquares.size());

	}	

	
	private PseudoMove createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleKnightMove(origen, PiecePositioned.of(destinoSquare, null));
	}
	
	private PseudoMove createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureKnightMove(origen, PiecePositioned.of(destinoSquare, destinoPieza));
	}
	
	private Collection<Square> toSquareCollection(long affectedBy) {
		Collection<Square> affectedBySquares = new ArrayList<Square>();
		for(int i = 0; i < 64; i++){
			if( (affectedBy & (1L << i))  != 0 ) {
				affectedBySquares.add(Square.squareByIdx(i));
			}			
		}
		return affectedBySquares;
	}
	
	private SquareBoard getTablero(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder();

		FENExporter exporter = new FENExporter(builder);

		exporter.export(FEN.of(string));

		return builder.getPositionRepresentation();
	}	
}
