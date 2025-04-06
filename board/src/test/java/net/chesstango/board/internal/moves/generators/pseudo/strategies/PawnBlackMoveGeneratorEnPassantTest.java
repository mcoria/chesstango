package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryBlack;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.State;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.internal.position.StateImp;
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
public class PawnBlackMoveGeneratorEnPassantTest {
	private PawnBlackMoveGenerator moveGenerator;
	
	private Collection<PseudoMove> moves;

	private MovePair<PseudoMove> movePair;

	private State state;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryBlack();
		moves = new ArrayList<>();
		state = new StateImp();
		
		moveGenerator = new PawnBlackMoveGenerator();
		moveGenerator.setPositionState(state);
		moveGenerator.setMoveFactory(moveFactoryImp);
	}

	@Test
	public void testPawnBlackPasanteDerecha() {
		SquareBoard tablero = getSquareBoard("8/8/8/8/3pP3/8/8/8");
		
		state.setEnPassantSquare(Square.e3);
		state.setCurrentTurn(Color.BLACK);

		moveGenerator.setSquareBoard(tablero);
		
		Square from = Square.d4;
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.e4));

		PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_BLACK);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureWhiteEnPassantMove(origen, Square.e3) ));
	}	

	@Test
	public void testPawnBlackPasanteIzquierda() {
		SquareBoard tablero = getSquareBoard("8/8/8/8/3Pp3/8/8/8");
		
		state.setEnPassantSquare(Square.d3);
		state.setCurrentTurn(Color.BLACK);

		moveGenerator.setSquareBoard(tablero);
		
		Square from = Square.e4;
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.d4));
		
		PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_BLACK);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureWhiteEnPassantMove(origen, Square.d3) ));
	}
	
	private PseudoMove createCaptureWhiteEnPassantMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createCaptureEnPassantPawnMove(origen, PiecePositioned.of(destinoSquare, null), PiecePositioned.of(Square.getSquare(destinoSquare.getFile(), 3), Piece.PAWN_WHITE), Cardinal.calculateSquaresDirection(origen.getSquare(), destinoSquare));
	}	
	
	private SquareBoard getSquareBoard(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder();
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getChessRepresentation();
	}	
	
}
