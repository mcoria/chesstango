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
import chess.iterators.Cardinal;
import chess.moves.Move;
import chess.moves.imp.MoveFactoryWhite;
import chess.position.PiecePlacement;
import chess.position.imp.ColorBoard;
import chess.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public class AbstractCardinalMoveGeneratorOesteTest {
	
	private AbstractCardinalMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moveGenerator = new AbstractCardinalMoveGenerator(Color.WHITE, new Cardinal[] {Cardinal.Oeste}){

			@Override
			protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
				return moveFactory.createSimpleMove(origen, destino);
			}

			@Override
			protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
				return moveFactory.createCaptureMove(origen, destino);
			}
			
		};
		moveGenerator.setMoveFactory(moveFactoryImp);
		
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void testOeste() {
		PiecePlacement tablero =  getTablero("8/8/8/4R3/8/8/8/8");
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.ROOK_WHITE);	
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.a5) ));
	}
	
	@Test
	public void testOeste01() {
		PiecePlacement tablero =  getTablero("8/8/8/B3R3/8/8/8/8");
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(from));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.a5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.ROOK_WHITE);	
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b5) ));
	}	
	
	@Test
	public void testOeste02() {
		PiecePlacement tablero = getTablero("8/8/8/b3R3/8/8/8/8");
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(from));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.a5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.ROOK_WHITE);	
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b5) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.a5, Piece.BISHOP_BLACK) ));
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
