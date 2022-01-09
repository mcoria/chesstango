package chess.pseudomovesgenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.CachePosiciones;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.ColorBoard;
import chess.layers.PosicionPiezaBoard;
import chess.moves.CastlingWhiteKingMove;
import chess.moves.CastlingWhiteQueenMove;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class KingWhiteMoveGeneratorTest {
	private KingWhiteMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private BoardState state;

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		state = new BoardState();
		
		moveGenerator = new KingWhiteMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactory);
	}
	
	@Test
	public void test01() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/4K3/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Piece.KING_WHITE, tablero.getPieza(from));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
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
		PosicionPiezaBoard tablero = getTablero("8/8/4P3/4K3/4p3/8/8/8");
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Piece.KING_WHITE, tablero.getPieza(from));		
		assertEquals(Piece.PAWN_WHITE, tablero.getPieza(Square.e6));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.e4));
		
		PiecePositioned origen = new PiecePositioned(from, Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
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
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/8/8/R3K3");
		
		state.setCastlingWhiteQueenPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));

		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(CachePosiciones.REY_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.REY_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new CastlingWhiteQueenMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastlingWhiteQueen02() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/5b2/8/R3K3");
		
		state.setCastlingWhiteQueenPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		

		assertEquals(Piece.KING_WHITE, tablero.getPieza(CachePosiciones.REY_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.f3));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.REY_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new CastlingWhiteQueenMove() ));
		
		assertEquals(6, moves.size());		
	}
	
	@Test
	public void testCastlingWhiteQueen03() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/5b2/8/8/R3K3");
		
		state.setCastlingWhiteQueenPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
	
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(CachePosiciones.REY_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.f4));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.REY_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new CastlingWhiteQueenMove() ));
		
		assertEquals(6, moves.size());	
	}
	
	@Test
	public void testCastlingWhiteQueen04() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/8/8/RN2K3");
		
		state.setCastlingWhiteQueenPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(CachePosiciones.REY_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		assertEquals(Piece.KNIGHT_WHITE, tablero.getPieza(Square.b1));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.REY_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( new CastlingWhiteQueenMove() ));
		
		assertEquals(5, moves.size());	
	}	
	
	@Test
	public void testCastlingWhiteKing01() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/8/8/4K2R");
		
		state.setCastlingWhiteKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(CachePosiciones.REY_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.REY_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new CastlingWhiteKingMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testCastlingWhiteKing02() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/3b4/8/4K2R");
		
		state.setCastlingWhiteKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));

		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(CachePosiciones.REY_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.d3));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.REY_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new CastlingWhiteKingMove() ));
		
		assertEquals(6, moves.size());		
	}
	
	@Test
	public void testCastlingWhiteKing03() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/3b4/8/8/4K2R");
		
		state.setCastlingWhiteKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(CachePosiciones.REY_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		assertEquals(Piece.BISHOP_BLACK, tablero.getPieza(Square.d4));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.REY_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new CastlingWhiteKingMove() ));
		
		assertEquals(6, moves.size());
	}		
	
	@Test
	public void testCastlingWhiteKing04() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/8/6p1/4K2R");
		
		state.setCastlingWhiteKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(CachePosiciones.REY_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		assertEquals(Piece.PAWN_BLACK, tablero.getPieza(Square.g2));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.REY_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new CastlingWhiteKingMove() ));
		
		assertEquals(6, moves.size());
	}		

	@Test
	public void testCastlingWhiteJaque() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/4r3/8/8/R3K2R");
		
		state.setCastlingWhiteKingPermitido(true);
		state.setCastlingWhiteQueenPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_WHITE, tablero.getPieza(CachePosiciones.REY_WHITE.getKey()));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.a1));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.h1));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.e4));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.REY_WHITE.getKey(), Piece.KING_WHITE);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertTrue(moves.contains( new CastlingWhiteKingMove() ));
		assertTrue(moves.contains( new CastlingWhiteQueenMove() ));
		
		assertEquals(7, moves.size());		
	}
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactory.createSimpleKingMoveBlanco(origen, new PiecePositioned(destinoSquare, null));
	}
	
	private Move createCaptureMove(PiecePositioned origen, Square destinoSquare, Piece destinoPieza) {
		return moveFactory.createCaptureKingMoveBlanco(origen, new PiecePositioned(destinoSquare, destinoPieza));
	}
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
