package chess.board.pseudomovesgenerators.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.builder.imp.PiecePlacementBuilder;
import chess.board.debug.builder.DebugChessFactory;
import chess.board.debug.chess.ColorBoardDebug;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.PositionState;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;
import chess.board.pseudomovesgenerators.strategies.KingBlackMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class KingBlackMoveGeneratorTest {

	private KingBlackMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private PositionState state;

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moves = new ArrayList<Move>();
		state = new PositionState();
		state.setTurnoActual(Color.BLACK);
		
		moveGenerator = new KingBlackMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactoryImp);
	}
	
	@Test
	public void testCastlingBlackQueen01() {
		PiecePlacement tablero =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastlingBlackQueen02() {
		PiecePlacement tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
	
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.f6));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastlingBlackQueen03() {
		PiecePlacement tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.f5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(6, moves.size());
	}		
	
	@Test
	public void testCastlingBlackKing01() {
		PiecePlacement tablero =  getTablero("4k2r/8/8/8/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testCastlingBlackKing02() {
		PiecePlacement tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);

		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.d6));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastlingBlackKing03() {
		PiecePlacement tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.d5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(6, moves.size());
	}		

	@Test
	public void testCastlingBlackJaque() {
		PiecePlacement tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		state.setCastlingBlackQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.e5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains(  moveFactoryImp.createCastlingKingMove() ));			// No se considera si el king esta en jaque
		assertTrue(moves.contains(  moveFactoryImp.createCastlingQueenMove() ));			// No se considera si el king esta en jaque
		
		assertEquals(7, moves.size());
	}
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleKingMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
	}

	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new DebugChessFactory());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}	
	
}
