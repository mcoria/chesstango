package chess.board.movesgenerators.pseudo.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import chess.board.debug.chess.ColorBoardDebug;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;
import chess.board.movesgenerators.pseudo.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public class KingBlackMoveGeneratorTest {

	private KingBlackMoveGenerator moveGenerator;
	
	private Collection<Move> moves;
	
	private MovePair moveCastling;
	
	private PositionState state;
	
	private ColorBoard colorBoard;
	
	protected KingCacheBoard kingCacheBoard;	

	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		state = new PositionState();
		state.setCurrentTurn(Color.BLACK);
		
		moveGenerator = new KingBlackMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactoryImp);
		
		colorBoard = new ColorBoardDebug();
		moveGenerator.setColorBoard(colorBoard);
		
		kingCacheBoard = new KingCacheBoard();
		moveGenerator.setKingCacheBoard(kingCacheBoard);
	}
	
	
	@Test
	public void test01() {
		PiecePlacement tablero =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
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
		
		assertEquals(5, moves.size());
	}
	
	@Test
	public void testCastlingBlackQueen01() {
		PiecePlacement tablero =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	
	@Test
	public void test02() {
		PiecePlacement tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
	
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
		
		assertEquals(5, moves.size());
	}	
	
	@Test
	public void testCastlingBlackQueen02() {
		PiecePlacement tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
	
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.f6));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	@Test
	public void test03() {
		PiecePlacement tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
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
		
		assertEquals(5, moves.size());
	}	
	
	@Test
	public void testCastlingBlackQueen03() {
		PiecePlacement tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.f5));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}		
	
	
	@Test
	public void test04() {
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
		
		assertEquals(5, moves.size());
	}
	
	@Test
	public void testCastlingBlackKing01() {
		PiecePlacement tablero =  getTablero("4k2r/8/8/8/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);

		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}	
	
	
	@Test
	public void test05() {
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
		
		assertEquals(5, moves.size());
	}
	
	@Test
	public void testCastlingBlackKing02() {
		PiecePlacement tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.d6));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	
	@Test
	public void test06() {
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
		
		assertEquals(5, moves.size());
	}	
	
	@Test
	public void testCastlingBlackKing03() {
		PiecePlacement tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.d5));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	
	@Test
	public void test07() {
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
		
		assertEquals(5, moves.size());
	}	

	@Test
	public void testCastlingBlackJaque() {
		PiecePlacement tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		state.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(PiecePositioned.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.e5));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();

		assertTrue(moveCastling.contains(  moveFactoryImp.createCastlingKingMove() ));				// No se considera si el king esta en jaque
		assertTrue(moveCastling.contains(  moveFactoryImp.createCastlingQueenMove() ));			// No se considera si el king esta en jaque
		
		assertEquals(2, moveCastling.size());
	}

	
	
	@Test
	public void testCapturedPositions() {
		PiecePlacement tablero =  getTablero("8/8/8/8/8/8/6k1/4K2R");
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.g2, Piece.KING_BLACK);
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(Square.g2));
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		long capturedPositions = generatorResult.getCapturedPositions();

		assertTrue( (Square.f1.getPosicion() & capturedPositions) != 0 );
		assertTrue( (Square.g1.getPosicion() & capturedPositions) != 0 );
		assertTrue( (Square.h1.getPosicion() & capturedPositions) != 0 );

		assertTrue( (Square.f2.getPosicion() & capturedPositions) != 0 );
		assertTrue( (Square.h2.getPosicion() & capturedPositions) != 0 );
		

		assertTrue( (Square.f3.getPosicion() & capturedPositions) != 0 );
		assertTrue( (Square.g3.getPosicion() & capturedPositions) != 0 );
		assertTrue( (Square.h3.getPosicion() & capturedPositions) != 0 );
	}	
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleKingMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
	}

	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		PiecePlacement tablero = builder.getResult();
		
		colorBoard.init(tablero);
		kingCacheBoard.init(tablero);
		
		moveGenerator.setPiecePlacement(tablero);
		
		return tablero;
	}	
	
}
