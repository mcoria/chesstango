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
import chess.builder.ChessPositionPartsBuilder;
import chess.debug.builder.DebugChessFactory;
import chess.iterators.Cardinal;
import chess.moves.Move;
import chess.moves.imp.MoveFactoryWhite;
import chess.parsers.FENParser;
import chess.position.ColorBoard;
import chess.position.PiecePlacement;
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.strategies.AbstractCardinalMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class AbstractCardinalMoveGeneratorNorteTest {
	
	private AbstractCardinalMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moveGenerator = new AbstractCardinalMoveGenerator(Color.WHITE, new Cardinal[] {Cardinal.Norte}){

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
	public void testNorte() {
		PiecePlacement tablero =  getTablero("8/8/8/4R3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.ROOK_WHITE);		
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e8) ));
	}
	
	@Test
	public void testNorte01() {
		PiecePlacement tablero =  getTablero("4B3/8/8/4R3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(from));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.e8));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.ROOK_WHITE);	
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
	}	
	
	@Test
	public void testNorte02() {
		PiecePlacement tablero =  getTablero("4b3/8/8/4R3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(from));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.e8));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.ROOK_WHITE);
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e8, Piece.BISHOP_BLACK) ));
	}	
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleMove(origen, new PiecePositioned(destinoSquare, null));
	}
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureMove(origen, new PiecePositioned(destinoSquare, destinoPieza));
	}
	
	private PiecePlacement getTablero(String string) {		
		ChessPositionPartsBuilder builder = new ChessPositionPartsBuilder(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPiecePlacement();
	}	
		
}