package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.PiecePlacementBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.ColorBoard;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.imp.ColorBoardImp;
import net.chesstango.board.position.KingSquare;
import net.chesstango.board.position.imp.KingSquareImp;
import net.chesstango.board.position.imp.PositionStateImp;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
	
	protected KingSquare kingSquare;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryWhite();
		state = new PositionStateImp();
		state.setCurrentTurn(Color.BLACK);
		
		moveGenerator = new KingBlackMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactoryImp);
		
		colorBoard = new ColorBoardDebug();
		moveGenerator.setColorBoard(colorBoard);
		
		kingSquare = new KingSquareImp();
		moveGenerator.setKingCacheBoard(kingSquare);
	}
	
	
	@Test
	public void test01() {
		Board tablero =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
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
		Board tablero =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	
	@Test
	public void test02() {
		Board tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
	
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.f6));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
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
		Board tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
	
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.f6));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	@Test
	public void test03() {
		Board tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.f5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
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
		Board tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8");
		
		state.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.f5));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}		
	
	
	@Test
	public void test04() {
		Board tablero =  getTablero("4k2r/8/8/8/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		
		moveGenerator.setBoard(tablero);
		
		ColorBoardImp colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
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
		Board tablero =  getTablero("4k2r/8/8/8/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);

		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}	
	
	
	@Test
	public void test05() {
		Board tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);

		moveGenerator.setBoard(tablero);
		
		ColorBoardImp colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.d6));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
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
		Board tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.d6));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	
	@Test
	public void test06() {
		Board tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		
		moveGenerator.setBoard(tablero);
		
		ColorBoardImp colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.d5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
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
		Board tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.d5));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	
	@Test
	public void test07() {
		Board tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		state.setCastlingBlackQueenAllowed(true);
		
		moveGenerator.setBoard(tablero);
		
		ColorBoardImp colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setColorBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.e5));
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
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
		Board tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8");
		
		state.setCastlingBlackKingAllowed(true);
		state.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.e5));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();

		assertTrue(moveCastling.contains(  moveFactoryImp.createCastlingKingMove() ));				// No se considera si el king esta en jaque
		assertTrue(moveCastling.contains(  moveFactoryImp.createCastlingQueenMove() ));			// No se considera si el king esta en jaque
		
		assertEquals(2, moveCastling.size());
	}

	
	
	@Test
	public void testCapturedPositions() {
		Board tablero =  getTablero("8/8/8/8/8/8/6k1/4K2R");
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.g2, Piece.KING_BLACK);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(Square.g2));
		
		MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
		
		long capturedPositions = generatorResult.getCapturedPositions();

		assertTrue( (Square.f1.getBitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.g1.getBitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.h1.getBitPosition() & capturedPositions) != 0 );

		assertTrue( (Square.f2.getBitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.h2.getBitPosition() & capturedPositions) != 0 );
		

		assertTrue( (Square.f3.getBitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.g3.getBitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.h3.getBitPosition() & capturedPositions) != 0 );
	}	
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleKingMove(origen, PiecePositioned.getPiecePositioned(destinoSquare, null));
	}

	private Board getTablero(String string) {
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		Board tablero = builder.getChessRepresentation();
		
		colorBoard.init(tablero);
		kingSquare.init(tablero);
		
		moveGenerator.setBoard(tablero);
		
		return tablero;
	}	
	
}
