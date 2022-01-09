package chess.pseudomovesgenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.PosicionPiezaBoard;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.parsers.FENParser;
import chess.pseudomovesgenerators.PawnPasanteMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class PawnPasanteMoveGeneratorTest {
	private PawnPasanteMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private BoardState state;

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		state = new BoardState();
		
		moveGenerator = new PawnPasanteMoveGenerator();
		moveGenerator.setBoardState(state);
	}
	



	
	@Test
	public void testPawnBlancoPasanteIzquierda() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/3pP3/8/8/8/8");
		
		state.setPawnPasanteSquare(Square.d6);
		state.setTurnoActual(Color.WHITE);
		
		moveGenerator.setTablero(tablero);

		Square from = Square.e5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.d5));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_WHITE);
		
		moves = moveGenerator.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureNegroPawnPasanteMove(origen, Square.d6) ));
	}
	
	

	
	@Test
	public void testPawnBlancoPasanteDerecha() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/3Pp3/8/8/8/8");
		
		state.setPawnPasanteSquare(Square.e6);
		state.setTurnoActual(Color.WHITE);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.e5));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_WHITE);
		
		moves = moveGenerator.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureNegroPawnPasanteMove(origen, Square.e6) ));
	}
	

	@Test
	public void testPawnNegroPasanteDerecha() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/3pP3/8/8/8");
		
		state.setPawnPasanteSquare(Square.e3);
		state.setTurnoActual(Color.BLACK);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d4;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.e4));

		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_BLACK);
		
		moves = moveGenerator.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureBlancoPawnPasanteMove(origen, Square.e3) ));
	}	

	@Test
	public void testPawnNegroPasanteIzquierda() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/3Pp3/8/8/8");
		
		state.setPawnPasanteSquare(Square.d3);
		state.setTurnoActual(Color.BLACK);		
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e4;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.d4));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.PAWN_BLACK);
		
		moves = moveGenerator.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureBlancoPawnPasanteMove(origen, Square.d3) ));
	}

	private Move createCaptureNegroPawnPasanteMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactory.createCapturePawnPasante(origen, new PiecePositioned(destinoSquare, null), new PiecePositioned(Square.getSquare(destinoSquare.getFile(), 4), Piece.PAWN_BLACK));
	}
	
	private Move createCaptureBlancoPawnPasanteMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactory.createCapturePawnPasante(origen, new PiecePositioned(destinoSquare, null), new PiecePositioned(Square.getSquare(destinoSquare.getFile(), 3), Piece.PAWN_WHITE));
	}	
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
