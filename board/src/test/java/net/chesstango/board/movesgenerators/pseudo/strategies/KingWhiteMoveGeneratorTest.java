package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.PiecePlacementBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.factory.MoveFactories;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.KingCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * @author Mauricio Coria
 *
 */
public class KingWhiteMoveGeneratorTest {
	
	private KingWhiteMoveGenerator moveGenerator;
	
	private Collection<Move> moves;
	
	private MovePair moveCastling;
	
	private PositionState state;
	
	private ColorBoard colorBoard;
	
	protected KingCacheBoard kingCacheBoard;

	private MoveFactory moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = MoveFactories.getDefaultMoveFactoryWhite();
		state = new PositionState();
		
		moveGenerator = new KingWhiteMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactoryImp);
		
		colorBoard = new ColorBoardDebug();
		moveGenerator.setColorBoard(colorBoard);
		
		kingCacheBoard = new KingCacheBoard();
		moveGenerator.setKingCacheBoard(kingCacheBoard);		
	}
	
	@Test
	public void test01() {
		PiecePlacement tablero =  getTablero("8/8/8/4K3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Piece.KING_WHITE, tablero.getPiece(from));
		
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
		
		Square from = Square.e5;
		assertEquals(Piece.KING_WHITE, tablero.getPiece(from));
		assertEquals(Piece.PAWN_WHITE, tablero.getPiece(Square.e6));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.e4));
		
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
	public void test03() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/8/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		moveGenerator.setPiecePlacement(tablero);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getSquare(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(5, moves.size());
	}
	
	@Test
	public void testCastlingWhiteQueen01() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/8/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	@Test
	public void test04() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/5b2/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);

		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.f3));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getSquare(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(5, moves.size());		
	}	
	
	@Test
	public void testCastlingWhiteQueen02() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/5b2/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);

		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.f3));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();		
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());		
	}
	
	@Test
	public void test05() {
		PiecePlacement tablero = getTablero("8/8/8/8/5b2/8/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);


		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.f4));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getSquare(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(5, moves.size());	
	}
	
	@Test
	public void testCastlingWhiteQueen03() {
		PiecePlacement tablero = getTablero("8/8/8/8/5b2/8/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.f4));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());	
	}
	
	
	@Test
	public void test06() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/8/8/RN2K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		assertEquals(Piece.KNIGHT_WHITE, tablero.getPiece(Square.b1));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getSquare(), Piece.KING_WHITE);
		
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
	public void testCastlingWhiteQueen04() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/8/8/RN2K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		assertEquals(Piece.KNIGHT_WHITE, tablero.getPiece(Square.b1));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();

		assertFalse(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(0, moveCastling.size());	
	}	
	
	
	@Test
	public void test07() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/8/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getSquare(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(5, moves.size());
	}	
	
	@Test
	public void testCastlingWhiteKing01() {
		PiecePlacement tablero = getTablero("8/8/8/8/8/8/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();

		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}	
	
	
	@Test
	public void test08() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/3b4/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.d3));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getSquare(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(5, moves.size());		
	}
	
	@Test
	public void testCastlingWhiteKing02() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/3b4/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.d3));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());		
	}
	
	
	@Test
	public void test09() {
		PiecePlacement tablero =  getTablero("8/8/8/8/3b4/8/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.d4));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getSquare(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(5, moves.size());
	}	
	
	@Test
	public void testCastlingWhiteKing03() {
		PiecePlacement tablero =  getTablero("8/8/8/8/3b4/8/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPiece(Square.d4));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}		
	
	
	@Test
	public void test10() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/8/6p1/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.g2));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getSquare(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(5, moves.size());
	}	
	
	@Test
	public void testCastlingWhiteKing04() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/8/6p1/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		assertEquals(Piece.PAWN_BLACK, tablero.getPiece(Square.g2));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}		

	@Test
	public void test11() {
		PiecePlacement tablero =  getTablero("8/8/8/8/4r3/8/8/R3K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		state.setCastlingWhiteQueenAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.e4));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_WHITE.getSquare(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(5, moves.size());		
	}
	
	@Test
	public void testCastlingWhiteJaque() {
		PiecePlacement tablero =  getTablero("8/8/8/8/4r3/8/8/R3K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		state.setCastlingWhiteQueenAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.e4));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(2, moveCastling.size());		
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
		
		PiecePlacement tablero = builder.getChessRepresentation();
		
		colorBoard.init(tablero);
		kingCacheBoard.init(tablero);
		
		moveGenerator.setPiecePlacement(tablero);
		
		return tablero;
	}
	
}
