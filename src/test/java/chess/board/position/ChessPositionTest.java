package chess.board.position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import chess.board.iterators.Cardinal;
import chess.board.moves.containers.MoveContainerReader;
import org.junit.Before;
import org.junit.Test;

import chess.board.Color;
import chess.board.GameState;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.analyzer.AnalyzerResult;
import chess.board.analyzer.PositionAnalyzer;
import chess.board.builder.imp.GameBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.factory.ChessFactory;
import chess.board.factory.ChessInjector;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.moves.imp.MoveFactoryWhite;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionTest {
	
	private MoveFactoryWhite moveFactoryWhite;
	
	private ChessFactory factory;
	
	private ChessInjector injector;
	
	private PositionAnalyzer analyzer; 
	
	private AnalyzerResult analyzerResult;
	
	private ChessPosition chessPosition;

	private GameState gameState;
	
	@Before
	public void setUp() throws Exception {		
		moveFactoryWhite = new MoveFactoryWhite();
		
		factory = new ChessFactoryDebug();
		
		injector = new ChessInjector(factory);
	}	
	
	@Test
	public void testDefaultPosition() {		
		settupWithDefaultBoard();

		MoveContainerReader moves = gameState.getLegalMoves();
		
		assertTrue(moves.contains( createSimpleMove(Square.a2, Piece.PAWN_WHITE, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.a2, Piece.PAWN_WHITE, Square.a4, Square.a3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.b2, Piece.PAWN_WHITE, Square.b3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.b2, Piece.PAWN_WHITE, Square.b4, Square.b3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.c2, Piece.PAWN_WHITE, Square.c3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.c2, Piece.PAWN_WHITE, Square.c4, Square.c3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.d2, Piece.PAWN_WHITE, Square.d3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.d2, Piece.PAWN_WHITE, Square.d4, Square.d3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.e2, Piece.PAWN_WHITE, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.e2, Piece.PAWN_WHITE, Square.e4, Square.e3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.f2, Piece.PAWN_WHITE, Square.f3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.f2, Piece.PAWN_WHITE, Square.f4, Square.f3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.g2, Piece.PAWN_WHITE, Square.g3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.g2, Piece.PAWN_WHITE, Square.g4, Square.g3) ));
		
		assertTrue(moves.contains( createSimpleMove(Square.h2, Piece.PAWN_WHITE, Square.h3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.h2, Piece.PAWN_WHITE, Square.h4, Square.h3) ));
		
		//Knight Kingna
		assertTrue(moves.contains( createSimpleMove(Square.b1, Piece.KNIGHT_WHITE, Square.a3) ));
		assertTrue(moves.contains( createSimpleMove(Square.b1, Piece.KNIGHT_WHITE, Square.c3) ));
		
		//Knight King
		assertTrue(moves.contains( createSimpleMove(Square.g1, Piece.KNIGHT_WHITE, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(Square.g1, Piece.KNIGHT_WHITE, Square.h3) ));
		
		//Debe haber 20 movimientos
		assertEquals(20, moves.size());
		
		//State
		assertEquals(Color.WHITE, chessPosition.getCurrentTurn());
		assertNull(chessPosition.getEnPassantSquare());
	}

	@Test
	public void testKingInCheck01() {
		settupWithBoard("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

		MoveContainerReader moves = gameState.getLegalMoves();

		assertEquals(Color.BLACK, chessPosition.getCurrentTurn());
		assertTrue(analyzerResult.isKingInCheck());

		assertTrue(moves.contains(createCaptureMove(Square.h6, Piece.KNIGHT_BLACK, Square.f7, Piece.QUEEN_WHITE)));
		assertFalse(moves.contains(createSimpleMove(Square.e8, Piece.KING_BLACK, Square.e7)));
		assertFalse(moves.contains(createCaptureMove(Square.e8, Piece.KING_BLACK, Square.f7, Piece.QUEEN_WHITE)));

		assertEquals(1, moves.size());
	}

	@Test
	public void testKingInCheck02() {
		settupWithBoard("rnb1kbnr/pp1ppppp/8/q1p5/8/3P4/PPPKPPPP/RNBQ1BNR w KQkq - 0 1");

		AnalyzerResult result = analyzer.analyze();

		assertEquals(Color.WHITE, chessPosition.getCurrentTurn());
		assertTrue(result.isKingInCheck());

		MoveContainerReader moves = gameState.getLegalMoves();

		assertTrue(moves.contains(createSimpleMove(Square.b1, Piece.KNIGHT_WHITE, Square.c3)));
		assertTrue(moves.contains(createSaltoDobleMove(Square.b2, Piece.PAWN_WHITE, Square.b4, Square.b3)));
		assertTrue(moves.contains(createSimpleMove(Square.c2, Piece.PAWN_WHITE, Square.c3)));
		assertTrue(moves.contains(createSimpleMove(Square.d2, Piece.KING_WHITE, Square.e3)));

		assertFalse(moves.contains(createSimpleMove(Square.d2, Piece.KING_WHITE, Square.e1)));

		assertEquals(4, moves.size());
	}

	@Test
	public void testJuegoCastlingWhiteJaque() {		
		settupWithBoard("r3k3/8/8/8/4r3/8/8/R3K2R w KQq - 0 1");

		AnalyzerResult result = analyzer.analyze();

		assertEquals(Color.WHITE, chessPosition.getCurrentTurn());
		assertTrue(result.isKingInCheck());

		MoveContainerReader moves = gameState.getLegalMoves();

		assertTrue(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.d1)));
		assertTrue(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.d2)));
		assertFalse(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.e2)));
		assertTrue(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.f2)));
		assertTrue(moves.contains(createSimpleKingMoveWhite(Square.e1, Square.f1)));

		assertFalse(moves.contains(moveFactoryWhite.createCastlingKingMove()));
		assertFalse(moves.contains(moveFactoryWhite.createCastlingQueenMove()));

		assertEquals(4, moves.size());
	}

	@Test
	public void testJuegoPawnPromocion() {
		settupWithBoard("r3k2r/p1ppqpb1/bn1Ppnp1/4N3/1p2P3/2N2Q2/PPPBBPpP/R4RK1 b kq - 0 2");

		MoveContainerReader moves = gameState.getLegalMoves();

		assertTrue(moves.contains(createCapturePawnPromocion(Square.g2, Piece.PAWN_BLACK, Square.f1, Piece.ROOK_WHITE,
				Piece.ROOK_BLACK)));
		assertTrue(moves.contains(createCapturePawnPromocion(Square.g2, Piece.PAWN_BLACK, Square.f1, Piece.ROOK_WHITE,
				Piece.KNIGHT_BLACK)));
		assertTrue(moves.contains(createCapturePawnPromocion(Square.g2, Piece.PAWN_BLACK, Square.f1, Piece.ROOK_WHITE,
				Piece.BISHOP_BLACK)));
		assertTrue(moves.contains(createCapturePawnPromocion(Square.g2, Piece.PAWN_BLACK, Square.f1, Piece.ROOK_WHITE,
				Piece.QUEEN_BLACK)));

		assertEquals(46, moves.size());
	}
	
	
	@Test
	public void testJauqeMate() {	
		settupWithBoard("r1bqk1nr/pppp1Qpp/2n5/2b1p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");
		
		AnalyzerResult result = analyzer.analyze();
		
		assertTrue(result.isKingInCheck());
		assertTrue(gameState.getLegalMoves().isEmpty());
		assertEquals(GameState.GameStatus.JAQUE_MATE, gameState.getStatus());
	
	}
	
	@Test
	public void testKingNoPuedeMoverAJaque(){
		settupWithBoard("8/8/8/8/8/8/6k1/4K2R w K - 0 1");

		MoveContainerReader moves = gameState.getLegalMoves();
		
		assertFalse(moves.contains(createSimpleMove(Square.e1, Piece.KING_WHITE, Square.f2)));
		assertFalse(moves.contains(createSimpleMove(Square.e1, Piece.KING_WHITE, Square.f1)));
		assertFalse(moves.contains(moveFactoryWhite.createCastlingKingMove()));
		
		assertEquals(12, moves.size());
		
	}
	
	@Test
	public void testMovimientoEnPassantNoAllowed(){
		settupWithBoard("8/2p5/3p4/KP5r/1R3pPk/8/4P3/8 b - g3 0 1");

		MoveContainerReader moves = gameState.getLegalMoves();
		
		assertFalse(moves.contains(createCaptureEnPassantMoveBlack(Square.f4, Square.g3)));
		
		assertEquals(17, moves.size());
		
	}	
	
	private Move createSimpleMove(Square origenSquare, Piece origenPieza, Square destinoSquare) {
		return moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(origenSquare, origenPieza), PiecePositioned.getPiecePositioned(destinoSquare, null));
	}
	
	private Move createCaptureMove(Square origenSquare, Piece origenPieza, Square destinoSquare, Piece destinoPieza) {
		return moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(origenSquare, origenPieza), PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza));
	}		

	private Move createSaltoDobleMove(Square origen, Piece piece, Square destinoSquare, Square squarePasante) {
		return moveFactoryWhite.createSaltoDoblePawnMove(PiecePositioned.getPiecePositioned(origen, piece), PiecePositioned.getPiecePositioned(destinoSquare, null),  squarePasante, Cardinal.Norte);
	}

	private Move createCapturePawnPromocion(Square origenSquare, Piece origenPieza, Square destinoSquare,
			Piece destinoPieza, Piece promocion) {
		return moveFactoryWhite.createCapturePawnPromocion(PiecePositioned.getPiecePositioned(origenSquare, origenPieza),
				PiecePositioned.getPiecePositioned(destinoSquare, destinoPieza), promocion);
	}
	
	private Move createCaptureEnPassantMoveBlack(Square origen, Square destinoSquare) {
		return moveFactoryWhite.createCaptureEnPassant(PiecePositioned.getPiecePositioned(origen, Piece.PAWN_BLACK),
				PiecePositioned.getPiecePositioned(destinoSquare, null), PiecePositioned.getPiecePositioned(
						Square.getSquare(destinoSquare.getFile(), destinoSquare.getRank() + 1), Piece.PAWN_WHITE));
	}
	
	private Move createSimpleKingMoveWhite(Square origen, Square destino) {
		return moveFactoryWhite.createSimpleKingMove(PiecePositioned.getPiecePositioned(origen, Piece.KING_WHITE), PiecePositioned.getPiecePositioned(destino, null));
	}	
	
	private void settupWithDefaultBoard() {
		settupWithBoard(FENDecoder.INITIAL_FEN);
	}	
	
	private void settupWithBoard(String string) {		
		GameBuilder builder = new GameBuilder(injector);

		FENDecoder parser = new FENDecoder(builder);
		parser.parseFEN(string);
		
		builder.getResult();
		
		chessPosition =  injector.getChessPosition();
		
		analyzer = injector.getAnalyzer();
		
		analyzerResult = analyzer.analyze();
		
		gameState = injector.getGameState();
	}		

		
}
