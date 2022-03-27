package chess.pseudomovesgenerators.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.builder.imp.PiecePlacementBuilder;
import chess.debug.builder.DebugChessFactory;
import chess.debug.chess.ColorBoardDebug;
import chess.fen.FENDecoder;
import chess.moves.Move;
import chess.moves.imp.MoveFactoryWhite;
import chess.position.PiecePlacement;
import chess.position.imp.ColorBoard;
import chess.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public class KnightMoveGeneratorTest {
	
	private KnightMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moveGenerator = new KnightMoveGenerator(Color.WHITE);
		moveGenerator.setMoveFactory(moveFactoryImp);
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void test() {
		PiecePlacement tablero =  getTablero("8/3P1p2/8/4N3/8/8/8/8");
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.KNIGHT_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.d7));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.f7));		
	
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.KNIGHT_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.g6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c6) ));
		// Pawn White en d7
		assertTrue(moves.contains( createCaptureMove(origen, Square.f7, Piece.PAWN_BLACK) ));
		
		assertEquals(7, moves.size());
	}

	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
	}
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza));
	}
	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new DebugChessFactory());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}	
	
}
