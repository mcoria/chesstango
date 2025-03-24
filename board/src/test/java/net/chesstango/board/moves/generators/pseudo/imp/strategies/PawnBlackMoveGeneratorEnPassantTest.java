package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryBlack;
import net.chesstango.board.moves.imp.MoveCommand;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.imp.PositionStateImp;
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
	
	private Collection<MoveCommand> moves;

	private MovePair movePair;

	private PositionState state;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryBlack();
		moves = new ArrayList<>();
		state = new PositionStateImp();
		
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

		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

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
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_BLACK);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureWhiteEnPassantMove(origen, Square.d3) ));
	}
	
	private Move createCaptureWhiteEnPassantMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createCaptureEnPassantPawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null), PiecePositioned.getPiecePositioned(Square.getSquare(destinoSquare.getFile(), 3), Piece.PAWN_WHITE), Cardinal.calculateSquaresDirection(origen.getSquare(), destinoSquare));
	}	
	
	private SquareBoard getSquareBoard(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getChessRepresentation();
	}	
	
}
