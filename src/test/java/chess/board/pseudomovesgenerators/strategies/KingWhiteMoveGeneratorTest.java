package chess.board.pseudomovesgenerators.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.builder.imp.PiecePlacementBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.debug.chess.ColorBoardDebug;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.PositionState;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;
import chess.board.pseudomovesgenerators.strategies.KingWhiteMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class KingWhiteMoveGeneratorTest {
	private KingWhiteMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private PositionState state;

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		moves = new ArrayList<Move>();
		state = new PositionState();
		
		moveGenerator = new KingWhiteMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactoryImp);
	}
	
	@Test
	public void test01() {
		PiecePlacement tablero =  getTablero("8/8/8/4K3/8/8/8/8");
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.KING_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		
		assertEquals(8, moves.size());
	}

	@Test
	public void test02() {
		PiecePlacement tablero = getTablero("8/8/4P3/4K3/4p3/8/8/8");
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		Square from = Square.e5;
		assertEquals(Piece.KING_WHITE, tablero.getPieza(from));		
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.e6));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.e4));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(from, Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e4, Piece.PAWN_BLACK) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		
		assertEquals(7, moves.size());
	}
	
	
	@Test
	public void testCastlingWhiteQueen01() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/8/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);

		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(PiecePositioned.KING_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastlingWhiteQueen02() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/5b2/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		

		assertEquals(Piece.KING_WHITE, tablero.getPieza(PiecePositioned.KING_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.f3));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(6, moves.size());		
	}
	
	@Test
	public void testCastlingWhiteQueen03() {
		PiecePlacement tablero = getTablero("8/8/8/8/5b2/8/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
	
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(PiecePositioned.KING_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.f4));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(6, moves.size());	
	}
	
	@Test
	public void testCastlingWhiteQueen04() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/8/8/RN2K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(PiecePositioned.KING_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		assertEquals(Piece.KNIGHT_WHITE, tablero.getPieza(Square.b1));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(5, moves.size());	
	}	
	
	@Test
	public void testCastlingWhiteKing01() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/8/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(PiecePositioned.KING_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testCastlingWhiteKing02() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/3b4/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);

		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(PiecePositioned.KING_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.d3));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(6, moves.size());		
	}
	
	@Test
	public void testCastlingWhiteKing03() {
		PiecePlacement tablero =  getTablero("8/8/8/8/3b4/8/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(PiecePositioned.KING_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.d4));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(6, moves.size());
	}		
	
	@Test
	public void testCastlingWhiteKing04() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/8/6p1/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);

		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(PiecePositioned.KING_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.g2));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(6, moves.size());
	}		

	@Test
	public void testCastlingWhiteJaque() {
		PiecePlacement tablero =  getTablero("8/8/8/8/4r3/8/8/R3K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		state.setCastlingWhiteQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		ColorBoard colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(PiecePositioned.KING_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.e4));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertTrue(moves.contains( moveFactoryImp.createCastlingKingMove() ));
		assertTrue(moves.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(7, moves.size());		
	}
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleKingMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
	}
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryImp.createCaptureKingMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza));
	}
	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}	
	
}
