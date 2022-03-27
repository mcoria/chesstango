package chess.pseudomovesgenerators.imp;

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
import chess.fen.FENDecoder;
import chess.moves.Move;
import chess.moves.imp.MoveFactoryWhite;
import chess.position.PiecePlacement;
import chess.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class PawnPasanteMoveGeneratorTest {
	private MoveGeneratorPawnPasanteImp moveGenerator;
	
	private Collection<Move> moves; 
	
	private PositionState state;

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moves = new ArrayList<Move>();
		state = new PositionState();
		
		moveGenerator = new MoveGeneratorPawnPasanteImp();
		moveGenerator.setBoardState(state);
	}
	



	
	@Test
	public void testPawnWhitePasanteIzquierda() {
		PiecePlacement tablero = getTablero("8/8/8/3pP3/8/8/8/8");
		
		state.setPawnPasanteSquare(Square.d6);
		state.setTurnoActual(Color.WHITE);
		
		moveGenerator.setTablero(tablero);

		Square from = Square.e5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.d5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);
		
		moves = moveGenerator.generatoPawnPasantePseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureBlackPawnPasanteMove(origen, Square.d6) ));
	}
	
	

	
	@Test
	public void testPawnWhitePasanteDerecha() {
		PiecePlacement tablero =  getTablero("8/8/8/3Pp3/8/8/8/8");
		
		state.setPawnPasanteSquare(Square.e6);
		state.setTurnoActual(Color.WHITE);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.e5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);
		
		moves = moveGenerator.generatoPawnPasantePseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureBlackPawnPasanteMove(origen, Square.e6) ));
	}
	

	@Test
	public void testPawnBlackPasanteDerecha() {
		PiecePlacement tablero = getTablero("8/8/8/8/3pP3/8/8/8");
		
		state.setPawnPasanteSquare(Square.e3);
		state.setTurnoActual(Color.BLACK);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d4;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.e4));

		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);
		
		moves = moveGenerator.generatoPawnPasantePseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureWhitePawnPasanteMove(origen, Square.e3) ));
	}	

	@Test
	public void testPawnBlackPasanteIzquierda() {
		PiecePlacement tablero = getTablero("8/8/8/8/3Pp3/8/8/8");
		
		state.setPawnPasanteSquare(Square.d3);
		state.setTurnoActual(Color.BLACK);		
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e4;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.d4));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);
		
		moves = moveGenerator.generatoPawnPasantePseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureWhitePawnPasanteMove(origen, Square.d3) ));
	}

	private Move createCaptureBlackPawnPasanteMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createCapturePawnPasante(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), PiecePositioned.getPiecePositioned(Square.getSquare(destinoSquare.getFile(), 4), Piece.PAWN_BLACK));
	}
	
	private Move createCaptureWhitePawnPasanteMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createCapturePawnPasante(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), PiecePositioned.getPiecePositioned(Square.getSquare(destinoSquare.getFile(), 3), Piece.PAWN_WHITE));
	}	
	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new DebugChessFactory());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}	
	
}
