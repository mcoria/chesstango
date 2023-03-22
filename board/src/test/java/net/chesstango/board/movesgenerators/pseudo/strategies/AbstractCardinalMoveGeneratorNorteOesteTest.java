package net.chesstango.board.movesgenerators.pseudo.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.PiecePlacementBuilder;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.imp.MoveFactoryWhite;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class AbstractCardinalMoveGeneratorNorteOesteTest {
	
	private AbstractCardinalMoveGenerator moveGenerator;
	
	private Collection<Move> moves;

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moveGenerator = new AbstractCardinalMoveGenerator(Color.WHITE, new Cardinal[] {Cardinal.NorteOeste}){

			@Override
			protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
				return moveFactory.createSimpleMove(origen, destino);
			}

			@Override
			protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
				return moveFactory.createCaptureMove(origen, destino);
			}
			
		};
		moveGenerator.setMoveFactory(moveFactoryImp);
		
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void testNorteOeste() {
		PiecePlacement tablero =  getTablero("8/8/8/4B3/8/8/8/8");
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(from));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.BISHOP_WHITE);
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b8) ));
	}
	
	
	
	@Test
	public void testNorteOeste01() {
		PiecePlacement tablero =  getTablero("1R6/8/8/4B3/8/8/8/8");
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(from));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.b8));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.BISHOP_WHITE);
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c7) ));
	}
	
	
	@Test
	public void testNorteOeste02() {
		PiecePlacement tablero =  getTablero("1r6/8/8/4B3/8/8/8/8");
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(from));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.b8));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.BISHOP_WHITE);
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c7) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.b8, Piece.ROOK_BLACK) ));
	}
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
	}
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza));
	}
	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getChessRepresentation();
	}	
}
