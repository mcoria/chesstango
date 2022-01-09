package chess.pseudomovesgenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.CachePosiciones;
import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.moves.CastlingBlackKingMove;
import chess.moves.CastlingBlackQueenMove;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.parsers.FENParser;
import chess.position.ColorBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class KingBlackMoveGeneratorTest {

	private KingBlackMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private PositionState state;

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		state = new PositionState();
		state.setTurnoActual(Color.BLACK);
		
		moveGenerator = new KingBlackMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactory);
	}
	
	@Test
	public void testCastlingBlackQueen01() {
		PiecePlacement tablero =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		state.setCastlingBlackQueenPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(CachePosiciones.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( new CastlingBlackQueenMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastlingBlackQueen02() {
		PiecePlacement tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8");
		
		state.setCastlingBlackQueenPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
	
		assertEquals(Piece.KING_BLACK, tablero.getPieza(CachePosiciones.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.f6));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( new CastlingBlackQueenMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastlingBlackQueen03() {
		PiecePlacement tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8");
		
		state.setCastlingBlackQueenPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(CachePosiciones.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.f5));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( new CastlingBlackQueenMove() ));
		
		assertEquals(6, moves.size());
	}		
	
	@Test
	public void testCastlingBlackKing01() {
		PiecePlacement tablero =  getTablero("4k2r/8/8/8/8/8/8/8");
		
		state.setCastlingBlackKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(CachePosiciones.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( new CastlingBlackKingMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testCastlingBlackKing02() {
		PiecePlacement tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8");
		
		state.setCastlingBlackKingPermitido(true);

		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(CachePosiciones.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.d6));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( new CastlingBlackKingMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastlingBlackKing03() {
		PiecePlacement tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8");
		
		state.setCastlingBlackKingPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(CachePosiciones.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		assertEquals(Piece.BISHOP_WHITE, tablero.getPieza(Square.d5));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( new CastlingBlackKingMove() ));
		
		assertEquals(6, moves.size());
	}		

	@Test
	public void testCastlingBlackJaque() {
		PiecePlacement tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8");
		
		state.setCastlingBlackKingPermitido(true);
		state.setCastlingBlackQueenPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Piece.KING_BLACK, tablero.getPieza(CachePosiciones.KING_BLACK.getKey()));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.a8));
		assertEquals(Piece.ROOK_BLACK, tablero.getPieza(Square.h8));
		assertEquals(Piece.ROOK_WHITE, tablero.getPieza(Square.e5));
		
		PiecePositioned origen = new PiecePositioned(CachePosiciones.KING_BLACK.getKey(), Piece.KING_BLACK);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains(  new CastlingBlackKingMove() ));			// No se considera si el king esta en jaque
		assertTrue(moves.contains(  new CastlingBlackQueenMove() ));			// No se considera si el king esta en jaque
		
		assertEquals(7, moves.size());
	}
	
	private Move createSimpleMove(PiecePositioned origen, Square destinoSquare) {
		return moveFactory.createSimpleKingMoveNegro(origen, new PiecePositioned(destinoSquare, null));
	}

	
	private PiecePlacement getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
