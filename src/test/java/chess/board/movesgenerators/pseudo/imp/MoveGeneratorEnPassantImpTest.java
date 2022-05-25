package chess.board.movesgenerators.pseudo.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import chess.board.moves.containers.MovePair;
import org.junit.Before;
import org.junit.Test;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.builder.imp.PiecePlacementBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorEnPassantImpTest {
	private MoveGeneratorEnPassantImp moveGenerator;
	
	private Collection<Move> moves;

	private MovePair movePair;

	private PositionState state;

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moves = new ArrayList<Move>();
		state = new PositionState();
		
		moveGenerator = new MoveGeneratorEnPassantImp();
		moveGenerator.setBoardState(state);
	}
	



	
	@Test
	public void testPawnWhitePasanteIzquierda() {
		PiecePlacement tablero = getTablero("8/8/8/3pP3/8/8/8/8");
		
		state.setEnPassantSquare(Square.d6);
		state.setCurrentTurn(Color.WHITE);
		
		moveGenerator.setTablero(tablero);

		Square from = Square.e5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.d5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureBlackEnPassantMove(origen, Square.d6) ));
	}
	
	

	
	@Test
	public void testPawnWhitePasanteDerecha() {
		PiecePlacement tablero =  getTablero("8/8/8/3Pp3/8/8/8/8");
		
		state.setEnPassantSquare(Square.e6);
		state.setCurrentTurn(Color.WHITE);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.e5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureBlackEnPassantMove(origen, Square.e6) ));
	}
	

	@Test
	public void testPawnBlackPasanteDerecha() {
		PiecePlacement tablero = getTablero("8/8/8/8/3pP3/8/8/8");
		
		state.setEnPassantSquare(Square.e3);
		state.setCurrentTurn(Color.BLACK);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d4;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.e4));

		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureWhiteEnPassantMove(origen, Square.e3) ));
	}	

	@Test
	public void testPawnBlackPasanteIzquierda() {
		PiecePlacement tablero = getTablero("8/8/8/8/3Pp3/8/8/8");
		
		state.setEnPassantSquare(Square.d3);
		state.setCurrentTurn(Color.BLACK);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e4;
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.d4));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureWhiteEnPassantMove(origen, Square.d3) ));
	}

	private Move createCaptureBlackEnPassantMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createCaptureEnPassant(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), PiecePositioned.getPiecePositioned(Square.getSquare(destinoSquare.getFile(), 4), Piece.PAWN_BLACK));
	}
	
	private Move createCaptureWhiteEnPassantMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createCaptureEnPassant(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), PiecePositioned.getPiecePositioned(Square.getSquare(destinoSquare.getFile(), 3), Piece.PAWN_WHITE));
	}	
	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}	
	
}
