package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.generators.pseudo.imp.strategies.PawnWhiteMoveGenerator;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.PositionState;
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
public class PawnWhiteMoveGeneratorEnPassantTest {
	private PawnWhiteMoveGenerator moveGenerator;
	
	private Collection<Move> moves;

	private MovePair movePair;

	private PositionState state;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryWhite();
		moves = new ArrayList<Move>();
		state = new PositionStateImp();
		
		moveGenerator = new PawnWhiteMoveGenerator();
		moveGenerator.setPositionState(state);
		moveGenerator.setMoveFactory(moveFactoryImp);
	}
	
	@Test
	public void testPawnWhitePasanteIzquierda() {
		SquareBoard tablero = getSquareBoard("8/8/8/3pP3/8/8/8/8");
		
		state.setEnPassantSquare(Square.d6);
		state.setCurrentTurn(Color.WHITE);
		
		moveGenerator.setSquareBoard(tablero);

		Square from = Square.e5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.d5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureBlackEnPassantMove(origen, Square.d6) ));
	}
	
	@Test
	public void testPawnWhitePasanteDerecha() {
		SquareBoard tablero =  getSquareBoard("8/8/8/3Pp3/8/8/8/8");
		
		state.setEnPassantSquare(Square.e6);
		state.setCurrentTurn(Color.WHITE);

		moveGenerator.setSquareBoard(tablero);
		
		Square from = Square.d5;
		assertEquals(Piece.PAWN_WHITE, tablero.getPiece(from));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.e5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.PAWN_WHITE);

		movePair = moveGenerator.generateEnPassantPseudoMoves();
		
		assertEquals(1, movePair.size());
		
		assertTrue(movePair.contains( createCaptureBlackEnPassantMove(origen, Square.e6) ));
	}

	private Move createCaptureBlackEnPassantMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createCaptureEnPassantPawnMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null),
				PiecePositioned.getPiecePositioned(Square.getSquare(destinoSquare.getFile(), 4),
				Piece.PAWN_BLACK), Cardinal.calculateSquaresDirection(origen.getSquare(), destinoSquare));
	}
	
	private SquareBoard getSquareBoard(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getChessRepresentation();
	}	
	
}
