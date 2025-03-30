package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.KingSquare;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.imp.KingSquareImp;
import net.chesstango.board.position.imp.PositionStateImp;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;



/**
 * @author Mauricio Coria
 *
 */
public class KingWhiteMoveGeneratorTest {
	
	private KingWhiteMoveGenerator moveGenerator;
	
	private Collection<PseudoMove> moves;
	
	private MovePair<PseudoMove> moveCastling;
	
	private PositionState state;
	
	private BitBoard bitBoard;
	
	protected KingSquare kingSquare;

	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		state = new PositionStateImp();
		
		moveGenerator = new KingWhiteMoveGenerator();
		moveGenerator.setPositionState(state);
		moveGenerator.setMoveFactory(moveFactoryImp);
		
		bitBoard = new BitBoardDebug();
		moveGenerator.setBitBoard(bitBoard);
		
		kingSquare = new KingSquareImp();
		moveGenerator.setKingSquare(kingSquare);
	}
	
	@Test
	public void test01() {
		SquareBoard tablero =  getTablero("8/8/8/4K3/8/8/8/8");
		
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
		SquareBoard tablero = getTablero("8/8/4P3/4K3/4p3/8/8/8");
		
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
		SquareBoard tablero = getTablero("8/8/8/8/8/8/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		moveGenerator.setSquareBoard(tablero);
		
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
		SquareBoard tablero = getTablero("8/8/8/8/8/8/8/R3K3");
		
		state.setCastlingWhiteQueenAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.a1));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();
		
		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingQueenMove() ));
		
		assertEquals(1, moveCastling.size());
	}
	
	@Test
	public void test04() {
		SquareBoard tablero = getTablero("8/8/8/8/8/5b2/8/R3K3");
		
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
		SquareBoard tablero = getTablero("8/8/8/8/8/5b2/8/R3K3");
		
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
		SquareBoard tablero = getTablero("8/8/8/8/5b2/8/8/R3K3");
		
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
		SquareBoard tablero = getTablero("8/8/8/8/5b2/8/8/R3K3");
		
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
		SquareBoard tablero = getTablero("8/8/8/8/8/8/8/RN2K3");
		
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
		SquareBoard tablero = getTablero("8/8/8/8/8/8/8/RN2K3");
		
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
		SquareBoard tablero = getTablero("8/8/8/8/8/8/8/4K2R");
		
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
		SquareBoard tablero = getTablero("8/8/8/8/8/8/8/4K2R");
		
		state.setCastlingWhiteKingAllowed(true);
		
		assertEquals(Piece.KING_WHITE, tablero.getPiece(PiecePositioned.KING_WHITE.getSquare()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPiece(Square.h1));
		
		moveCastling = moveGenerator.generateCastlingPseudoMoves();

		assertTrue(moveCastling.contains( moveFactoryImp.createCastlingKingMove() ));
		
		assertEquals(1, moveCastling.size());
	}	
	
	
	@Test
	public void test08() {
		SquareBoard tablero =  getTablero("8/8/8/8/8/3b4/8/4K2R");
		
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
		SquareBoard tablero =  getTablero("8/8/8/8/8/3b4/8/4K2R");
		
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
		SquareBoard tablero =  getTablero("8/8/8/8/3b4/8/8/4K2R");
		
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
		SquareBoard tablero =  getTablero("8/8/8/8/3b4/8/8/4K2R");
		
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
		SquareBoard tablero =  getTablero("8/8/8/8/8/8/6p1/4K2R");
		
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
		SquareBoard tablero =  getTablero("8/8/8/8/8/8/6p1/4K2R");
		
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
		SquareBoard tablero =  getTablero("8/8/8/8/4r3/8/8/R3K2R");
		
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
		SquareBoard tablero =  getTablero("8/8/8/8/4r3/8/8/R3K2R");
		
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
	
	private SquareBoard getTablero(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder(new ChessFactoryDebug());
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		SquareBoard tablero = builder.getChessRepresentation();
		
		bitBoard.init(tablero);
		kingSquare.init(tablero);
		
		moveGenerator.setSquareBoard(tablero);
		
		return tablero;
	}
	
}
