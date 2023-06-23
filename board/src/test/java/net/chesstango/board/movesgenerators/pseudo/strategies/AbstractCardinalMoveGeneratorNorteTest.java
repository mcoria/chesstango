package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.PiecePlacementBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.BitBoard;
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
public class AbstractCardinalMoveGeneratorNorteTest {
	
	private AbstractCardinalMoveGenerator moveGenerator;
	
	private Collection<Move> moves;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryWhite();
		moveGenerator = new AbstractCardinalMoveGenerator(Color.WHITE, new Cardinal[] {Cardinal.Norte}){

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
	public void testNorte() {
		SquareBoard tablero =  getTablero("8/8/8/4R3/8/8/8/8");
		moveGenerator.setSquareBoard(tablero);

		BitBoard bitBoard = new BitBoardDebug();
		bitBoard.init(tablero);
		moveGenerator.setBitBoard(bitBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(from));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.ROOK_WHITE);		
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e8) ));
	}
	
	@Test
	public void testNorte01() {
		SquareBoard tablero =  getTablero("4B3/8/8/4R3/8/8/8/8");
		moveGenerator.setSquareBoard(tablero);

		BitBoard bitBoard = new BitBoardDebug();
		bitBoard.init(tablero);
		moveGenerator.setBitBoard(bitBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(from));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.e8));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.ROOK_WHITE);	
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
	}	
	
	@Test
	public void testNorte02() {
		SquareBoard tablero =  getTablero("4b3/8/8/4R3/8/8/8/8");
		moveGenerator.setSquareBoard(tablero);

		BitBoard bitBoard = new BitBoardDebug();
		bitBoard.init(tablero);
		moveGenerator.setBitBoard(bitBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(from));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.e8));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.ROOK_WHITE);
	
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e8, Piece.BISHOP_BLACK) ));
	}	
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
	}
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza));
	}
	
	private SquareBoard getTablero(String string) {
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getChessRepresentation();
	}	
		
}
