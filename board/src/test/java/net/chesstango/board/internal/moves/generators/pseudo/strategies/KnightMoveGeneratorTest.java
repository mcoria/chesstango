package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.internal.position.BitBoardDebug;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENExporter;
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
public class KnightMoveGeneratorTest {
	
	private KnightMoveGenerator moveGenerator;
	
	private Collection<PseudoMove> moves;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moveGenerator = new KnightMoveGenerator(Color.WHITE);
		moveGenerator.setMoveFactory(moveFactoryImp);
		moves = new ArrayList<>();
	}
	
	@Test
	public void test() {
		SquareBoard tablero =  getTablero("8/3P1p2/8/4N3/8/8/8/8 w KQkq - 0 1");
		moveGenerator.setSquareBoard(tablero);

		BitBoard bitBoard = new BitBoardDebug();
		bitBoard.init(tablero);
		moveGenerator.setBitBoard(bitBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.KNIGHT_WHITE, tablero.getPiece(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.d7));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.f7));
	
		PiecePositioned origen = PiecePositioned.of(from, Piece.KNIGHT_WHITE);
		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.g6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c6) ));
		// Pawn White en d7
		assertTrue(moves.contains( createCaptureMove(origen, Square.f7, Piece.PAWN_BLACK) ));
		
		assertEquals(7, moves.size());
	}

	private PseudoMove createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleKnightMove(origen, PiecePositioned.of(destinoSquare, null));
	}
	
	private PseudoMove createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureKnightMove(origen, PiecePositioned.of(destinoSquare, destinoPieza));
	}
	
	private SquareBoard getTablero(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder();

		FENExporter exporter = new FENExporter(builder);

		exporter.export(FEN.of(string));

		return builder.getPositionRepresentation();
	}	
	
}
