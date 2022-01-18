package chess.pseudomovesgenerators;

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

/**
 * @author Mauricio Coria
 *
 */
public class AbstractCardinalMoveGeneratorSurEsteTest {
	
	private AbstractCardinalMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moveGenerator = new AbstractCardinalMoveGenerator(Color.WHITE, new Cardinal[] {Cardinal.SurEste}){

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
	public void testSurEste() {
		PiecePlacement tablero =  getTablero("8/8/8/4B3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.BISHOP_WHITE);
	
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.h2) ));
	}
	
	
	@Test
	public void testSurEste01() {
		PiecePlacement tablero = getTablero("8/8/8/4B3/8/8/7R/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(from));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h2));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.BISHOP_WHITE);
	
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g3) ));
	}	
	
	
	@Test
	public void testSurEste02() {
		PiecePlacement tablero = getTablero("8/8/8/4B3/8/8/7r/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(from));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h2));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.BISHOP_WHITE);
	
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.h2, Piece.ROOK_BLACK) ));
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
