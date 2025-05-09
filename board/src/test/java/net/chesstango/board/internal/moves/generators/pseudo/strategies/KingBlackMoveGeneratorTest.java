package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.internal.position.BitBoardDebug;
import net.chesstango.board.internal.position.PositionStateImp;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.KingSquare;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.internal.position.BitBoardImp;
import net.chesstango.board.internal.position.KingSquareImp;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENExporter;
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
	
	private Collection<PseudoMove> moves;
	
	private MovePair<PseudoMove> moveCastling;
	
	private PositionState positionState;
	
	private BitBoard bitBoard;
	
	protected KingSquare kingSquare;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		positionState = new PositionStateImp();
		positionState.setCurrentTurn(Color.BLACK);
		
		moveGenerator = new KingBlackMoveGenerator();
		moveGenerator.setPositionState(positionState);
		moveGenerator.setMoveFactory(moveFactoryImp);
		
		bitBoard = new BitBoardDebug();
		moveGenerator.setBitBoard(bitBoard);
		
		kingSquare = new KingSquareImp();
		moveGenerator.setKingSquare(kingSquare);
	}
	
	
	@Test
	public void test01() {
		SquareBoard tablero =  getTablero("r3k3/8/8/8/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		
		PiecePositioned origen = PiecePositioned.of(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
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
		SquareBoard tablero =  getTablero("r3k3/8/8/8/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	
	@Test
	public void test02() {
		SquareBoard tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackQueenAllowed(true);
	
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.f6));
		
		PiecePositioned origen = PiecePositioned.of(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
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
		SquareBoard tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackQueenAllowed(true);
	
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.f6));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	@Test
	public void test03() {
		SquareBoard tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.f5));
		
		PiecePositioned origen = PiecePositioned.of(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
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
		SquareBoard tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackQueenAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.f5));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}		
	
	
	@Test
	public void test04() {
		SquareBoard tablero =  getTablero("4k2r/8/8/8/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackKingAllowed(true);
		
		moveGenerator.setSquareBoard(tablero);
		
		BitBoardImp colorBoard = new BitBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setBitBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		
		PiecePositioned origen = PiecePositioned.of(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
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
		SquareBoard tablero =  getTablero("4k2r/8/8/8/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackKingAllowed(true);

		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}	
	
	
	@Test
	public void test05() {
		SquareBoard tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackKingAllowed(true);

		moveGenerator.setSquareBoard(tablero);
		
		BitBoardImp colorBoard = new BitBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setBitBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.d6));
		
		PiecePositioned origen = PiecePositioned.of(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
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
		SquareBoard tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackKingAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.d6));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	
	@Test
	public void test06() {
		SquareBoard tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackKingAllowed(true);
		
		moveGenerator.setSquareBoard(tablero);
		
		BitBoardImp colorBoard = new BitBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setBitBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.d5));
		
		PiecePositioned origen = PiecePositioned.of(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
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
		SquareBoard tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackKingAllowed(true);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPiece(Square.d5));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	
	@Test
	public void test07() {
		SquareBoard tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackKingAllowed(true);
		positionState.setCastlingBlackQueenAllowed(true);
		
		moveGenerator.setSquareBoard(tablero);
		
		BitBoardImp colorBoard = new BitBoardDebug();
		colorBoard.init(tablero);
		moveGenerator.setBitBoard(colorBoard);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(PiecePositioned.KING_BLACK.getSquare()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.a8));
		assertEquals(Piece.ROOK_BLACK, tablero.getPiece(Square.h8));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.e5));
		
		PiecePositioned origen = PiecePositioned.of(PiecePositioned.KING_BLACK.getSquare(), Piece.KING_BLACK);
		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
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
		SquareBoard tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8 w KQkq - 0 1");
		
		positionState.setCastlingBlackKingAllowed(true);
		positionState.setCastlingBlackQueenAllowed(true);
		
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
		SquareBoard tablero =  getTablero("8/8/8/8/8/8/6k1/4K2R w KQkq - 0 1");
		
		PiecePositioned origen = PiecePositioned.of(Square.g2, Piece.KING_BLACK);
		
		assertEquals(Piece.KING_BLACK, tablero.getPiece(Square.g2));
		
		MoveGeneratorByPieceResult generatorResult = moveGenerator.generateByPiecePseudoMoves(origen);
		
		long capturedPositions = generatorResult.getCapturedPositions();

		assertTrue( (Square.f1.bitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.g1.bitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.h1.bitPosition() & capturedPositions) != 0 );

		assertTrue( (Square.f2.bitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.h2.bitPosition() & capturedPositions) != 0 );
		

		assertTrue( (Square.f3.bitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.g3.bitPosition() & capturedPositions) != 0 );
		assertTrue( (Square.h3.bitPosition() & capturedPositions) != 0 );
	}	
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactoryImp.createSimpleKingMove(origen, PiecePositioned.of(destinoSquare, null));
	}

	private SquareBoard getTablero(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder();

		FENExporter exporter = new FENExporter(builder);

		exporter.export(FEN.of(string));
		
		SquareBoard tablero = builder.getPositionRepresentation();
		
		bitBoard.init(tablero);
		kingSquare.init(tablero);
		
		moveGenerator.setSquareBoard(tablero);
		
		return tablero;
	}	
	
}
