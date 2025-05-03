package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.internal.position.PositionStateImp;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENExporter;
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
public class PawnWhiteMoveGeneratorEnPassantTest {
	private PawnWhiteMoveGenerator moveGenerator;
	
	private Collection<PseudoMove> moves;

	private MovePair<PseudoMove> movePair;

	private PositionState positionState;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moves = new ArrayList<>();
		positionState = new PositionStateImp();
		
		moveGenerator = new PawnWhiteMoveGenerator();
		moveGenerator.setPositionState(positionState);
		moveGenerator.setMoveFactory(moveFactoryImp);
	}
	
	@Test
	public void testPawnWhitePasanteIzquierda() {
		SquareBoard tablero = getSquareBoard("8/8/8/3pP3/8/8/8/8 w KQkq - 0 1");
		
		positionState.setEnPassantSquare(Square.d6);
		positionState.setCurrentTurn(Color.WHITE);
		
		moveGenerator.setSquareBoard(tablero);

		Square from = Square.e5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.d5));
		
		PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_WHITE);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureBlackEnPassantMove(origen, Square.d6) ));
	}
	
	@Test
	public void testPawnWhitePasanteDerecha() {
		SquareBoard tablero =  getSquareBoard("8/8/8/3Pp3/8/8/8/8 w KQkq - 0 1");
		
		positionState.setEnPassantSquare(Square.e6);
		positionState.setCurrentTurn(Color.WHITE);

		moveGenerator.setSquareBoard(tablero);
		
		Square from = Square.d5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.e5));
		
		PiecePositioned origen = PiecePositioned.of(from, Piece.PAWN_WHITE);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureBlackEnPassantMove(origen, Square.e6) ));
	}

	private PseudoMove createCaptureBlackEnPassantMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createCaptureEnPassantPawnMove(origen, PiecePositioned.of(destinoSquare, null),
				PiecePositioned.of(Square.getSquare(destinoSquare.getFile(), 4),
				Piece.PAWN_BLACK), Cardinal.calculateSquaresDirection(origen.getSquare(), destinoSquare));
	}
	
	private SquareBoard getSquareBoard(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder();

		FENExporter exporter = new FENExporter(builder);

		exporter.export(FEN.of(string));

		return builder.getPositionRepresentation();
	}	
	
}
