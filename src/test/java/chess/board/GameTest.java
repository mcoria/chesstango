package chess.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import chess.board.moves.containers.MoveContainerReader;
import org.junit.Test;

import chess.board.builder.imp.GameBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.representations.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.moves.imp.MoveFactoryBlack;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.ChessPositionReader;


/**
 * @author Mauricio Coria
 *
 */
public class GameTest {

	
	@Test
	public void testInitialPosition() {
		Game game =  getDefaultGame();
		
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		assertTrue(game.getChessPosition().isCastlingWhiteQueenAllowed());
		assertTrue(game.getChessPosition().isCastlingWhiteKingAllowed());
		assertTrue(game.getChessPosition().isCastlingBlackQueenAllowed());
		assertTrue(game.getChessPosition().isCastlingBlackKingAllowed());
		assertEquals(0, game.getChessPosition().getHalfMoveClock());
		assertEquals(1, game.getChessPosition().getFullMoveClock());

		assertEquals(GameState.GameStatus.IN_PROGRESS, game.getGameStatus());
		assertEquals(20, game.getPossibleMoves().size());
	}

	@Test
	public void test_mate() {
		Game game =  getDefaultGame();
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		
		game.executeMove(Square.e2, Square.e4)
		.executeMove(Square.e7, Square.e5)
		.executeMove(Square.f1, Square.c4)
		.executeMove(Square.b8, Square.c6)
		.executeMove(Square.d1, Square.f3)
		.executeMove(Square.f8, Square.c5)
		.executeMove(Square.f3, Square.f7);
		
		assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
		assertEquals(GameState.GameStatus.MATE, game.getGameStatus());
		assertTrue(game.getPossibleMoves().isEmpty());
		assertEquals(0, game.getChessPosition().getHalfMoveClock());
		assertEquals(4, game.getChessPosition().getFullMoveClock());
	}

	@Test
	public void test_mateAndUndo() {
		Game game =  getDefaultGame();
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		
		game.executeMove(Square.e2, Square.e4)
		.executeMove(Square.e7, Square.e5)
		.executeMove(Square.f1, Square.c4)
		.executeMove(Square.b8, Square.c6)
		.executeMove(Square.d1, Square.f3)
		.executeMove(Square.f8, Square.c5)
		.executeMove(Square.f3, Square.f7);
		
		assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
		assertEquals(GameState.GameStatus.MATE, game.getGameStatus());
		assertTrue(game.getPossibleMoves().isEmpty());
		assertEquals(0, game.getChessPosition().getHalfMoveClock());
		assertEquals(4, game.getChessPosition().getFullMoveClock());
		
		game.undoMove()
		.undoMove()
		.undoMove()
		.undoMove()
		.undoMove()
		.undoMove()
		.undoMove();
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		assertEquals(0, game.getChessPosition().getHalfMoveClock());
		assertEquals(1, game.getChessPosition().getFullMoveClock());
	}	
	
	@Test
	public void testJuegoJaque() {
		Game game =  getDefaultGame();
		
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		
		game.executeMove(Square.e2, Square.e4);
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
		
		game.executeMove(Square.e7, Square.e5);
		assertEquals(29, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		
		game.executeMove(Square.f1, Square.c4);
		assertEquals(29, game.getPossibleMoves().size());
		assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
		
		game.executeMove(Square.b8, Square.c6);
		assertEquals(33, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		
		game.executeMove(Square.d1, Square.f3);
		assertEquals(31, game.getPossibleMoves().size());
		assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
		
		game.executeMove(Square.g8, Square.h6);
		assertEquals(42, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		
		
		game.executeMove(Square.f3, Square.f7);
		assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
		assertEquals(GameState.GameStatus.CHECK, game.getGameStatus());
		assertEquals(1, game.getPossibleMoves().size());
	}
	
	@Test
	public void testJuegoTablas() {
		Game game =  getGame("k7/7Q/K7/8/8/8/8/8 w - - 0 1");
		
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		assertEquals(0, game.getChessPosition().getHalfMoveClock());
		assertEquals(1, game.getChessPosition().getFullMoveClock());
		
		game.executeMove(Square.h7, Square.c7);

		assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
		assertEquals(GameState.GameStatus.DRAW, game.getGameStatus());
		assertEquals(0, game.getPossibleMoves().size());
		assertEquals(1, game.getChessPosition().getHalfMoveClock());
		assertEquals(1, game.getChessPosition().getFullMoveClock());
	}
	
	@Test
	public void test_undo() {
		Game game =  getDefaultGame();
		
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		assertEquals(0, game.getChessPosition().getHalfMoveClock());
		assertEquals(1, game.getChessPosition().getFullMoveClock());
		
		game.executeMove(Square.e2, Square.e4);
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
		assertEquals(0, game.getChessPosition().getHalfMoveClock());
		assertEquals(1, game.getChessPosition().getFullMoveClock());
		
		game.undoMove();
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getChessPosition().getCurrentTurn());
		assertEquals(0, game.getChessPosition().getHalfMoveClock());
		assertEquals(1, game.getChessPosition().getFullMoveClock());
	}
	
	@Test
	public void testJuegoNoEnPassant() {
		Game game =  getGame("rnbqkbnr/p1pppppp/1p6/P7/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 2");
		
		game.executeMove(Square.b6, Square.b5);
		
		assertEquals(22, game.getPossibleMoves().size());
		assertEquals(0, game.getChessPosition().getHalfMoveClock());
		assertEquals(3, game.getChessPosition().getFullMoveClock()); // Movio Negras
	}
	
	@Test
	public void testJuegoEnPassant() {
		Game game =  getGame("rnbqkbnr/1ppppppp/8/pP6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2");
		
		game.executeMove(Square.c7, Square.c5);

		assertEquals(Square.c6, game.getChessPosition().getEnPassantSquare());
		assertNotNull(game.getMove(Square.b5, Square.c6));
		assertEquals(22, game.getPossibleMoves().size());
	}
	
	
	//TODO: No solo cuando se mueve la torre sino tambien cuando la capturan
	@Test 
	public void testMueveRookPierdeEnroque() {
		Game game =  getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
		
		game.executeMove(Square.a1, Square.b1);
		
		assertFalse(game.getChessPosition().isCastlingWhiteQueenAllowed());
		
		assertEquals(43, game.getPossibleMoves().size());
	}	

	@Test
	public void testJuegoKiwipeteTestUndo() {
		Game game =  getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
		
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e1, Square.g1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e1, Square.c1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e1, Square.f1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e1, Square.d1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.d5, Square.d6);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.d5, Square.e6);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.a2, Square.a3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.a2, Square.a4);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.b2, Square.b3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.g2, Square.g3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.g2, Square.g4);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.g2, Square.h3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e5, Square.d3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e5, Square.f7);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e5, Square.c4);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e5, Square.g6);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e5, Square.g4);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e5, Square.c6);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e5, Square.d7);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.c3, Square.b1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.c3, Square.a4);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.c3, Square.d1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.c3, Square.b5);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.f3, Square.g3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.f3, Square.h3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.f3, Square.e3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.f3, Square.d3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.f3, Square.g4);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.f3, Square.h5);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.f3, Square.f4);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.f3, Square.f5);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.f3, Square.f6);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.d2, Square.c1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.d2, Square.e3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.d2, Square.f4);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.d2, Square.g5);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.d2, Square.h6);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e2, Square.d1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e2, Square.f1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e2, Square.d3);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e2, Square.c4);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e2, Square.b5);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.e2, Square.a6);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.a1, Square.b1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.a1, Square.c1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.a1, Square.d1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.h1, Square.g1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
		game.executeMove(Square.h1, Square.f1);
		game.undoMove();
		assertEquals(48, game.getPossibleMoves().size());
		
	}
	
	@Test
	public void testUndoCaptureRook() {
		Game game =  getGame("4k2r/8/8/8/3B4/8/8/4K3 w k - 0 1");
		
		//Estado inicial
		assertEquals(18, game.getPossibleMoves().size());
		
		//Movimiento 1 - cualquier movimiento
		game.executeMove(Square.d4, Square.c3);
		assertEquals(15, game.getPossibleMoves().size()); 
		//CastlingBlackKingMove es uno de los movimientos posibles
		assertEquals(MoveFactoryBlack.castlingKingMove, game.getMove(Square.e8, Square.g8));
		
		//Undo movimiento 1 y volvemos al estado inicial
		game.undoMove();
		
		//Estado inicial
		assertEquals(18, game.getPossibleMoves().size());
		
		//Capturamos la torre negra
		game.executeMove(Square.d4, Square.h8);
		assertEquals(5, game.getPossibleMoves().size());
		//Ya no tenemos castling
		assertNull(game.getMove(Square.e8, Square.h8));
		
		//Undo captura de torre negra ---- Volvemos al estado inicial
		game.undoMove();  // Aca esta el problema, el UNDO no borra los movimientos de king del cache
		assertEquals(18, game.getPossibleMoves().size());
		
		//Movimiento 1 - lo repetimos
		game.executeMove(Square.d4, Square.c3);
		assertEquals(15, game.getPossibleMoves().size()); 
		//CastlingBlackKingMove es uno de los movimientos posibles
		assertEquals(MoveFactoryBlack.castlingKingMove, game.getMove(Square.e8, Square.g8));
	}
	
	
	@Test
	public void testUndoRookMove() {
		Game game = getGame("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
		
		//Estado inicial
		assertEquals(26, game.getPossibleMoves().size());
		
		//Mueve la torre blanca de king
		game.executeMove(Square.h1, Square.h2);
		
		//Mueve king negro
		game.executeMove(Square.e8, Square.e7);
		
		game.undoMove();
		
		game.undoMove();
		
		//Estado inicial
		assertEquals(26, game.getPossibleMoves().size());		
	}
	
	@Test
	public void testKinkWhitePierdeCastling() {
		Game game = getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
		
		game.executeMove(Square.a1, Square.b1);
		
		game.executeMove(Square.h3, Square.g2);
		
		game.executeMove(Square.b1, Square.c1);
		
		game.executeMove(Square.g2, Square.h1);
		
		ChessPositionReader reader = game.getChessPosition();
		
		assertFalse( reader.isCastlingWhiteKingAllowed() );
	}
	

	@Test
	public void testCacheEnEstadoInvalido01() {
		Game game = getGame("4k3/8/8/8/4b3/8/8/R3K2R w KQ - 0 1");
		
		//Antes de mover blanca podemos ver que tenemos enroque 
		assertTrue("castlingKingMove not present", game.getPossibleMoves().contains(MoveFactoryWhite.castlingKingMove));

		// Mueve torre blanca 
		game.executeMove(Square.a1, Square.d1);
		// .... y limpia los movimientos de cache de REY
		
		// Mueve Negra y capture torre blanca de Rey
		game.executeMove(Square.e4, Square.h1);
		
		// Blanca pierde el enroque de rey
		// Rey establece los movimientos en cache (sin enroque de Torre Rey)
		// Los movimientos que establece en cache no dependen de lo que hay en h1 (puesto que no hay torre blanca)
		assertFalse("castlingKingMove not present", game.getPossibleMoves().contains(MoveFactoryWhite.castlingKingMove));		

		// Aca comienza el bolonqui
		game.undoMove();
		// El undo deja los movimientos de REY recien almacenados en cache
		
		// Negra NO come torre de Rey
		game.executeMove(Square.e4, Square.d5);
		// Y cuando se pregunta por movimientos de rey en cache
		
		//Blanca deberia tener enroque de rey nuevamente
		assertTrue("castlingKingMove not present", game.getPossibleMoves().contains(MoveFactoryWhite.castlingKingMove));
		
		assertFalse(game.getChessPosition().isCastlingWhiteQueenAllowed());

	}	
	
	
	@Test
	public void testJuegoEnPassantUndo() {
		MoveContainerReader legalMoves = null;

		Game game = getGame("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2");
		
		legalMoves = game.getPossibleMoves();		

		// Estado inicial
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));

		// Mueve el pawn pasante
		game.executeMove(Square.c7, Square.c5);


		// Podemos capturarlo
		legalMoves = game.getPossibleMoves();	
		assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
		assertEquals(22, legalMoves.size());

		// Volvemos atras
		game.undoMove();

		// No podemos capturarlo
		legalMoves = game.getPossibleMoves();	
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
	}

	@Test
	public void testJuegoEnPassant01() {
		MoveContainerReader legalMoves = null;
		
		Game game = getGame("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2");

		// Estado inicial
		legalMoves = game.getPossibleMoves();
		
		assertEquals(19, legalMoves.size());
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
		
		// Mueve el pawn pasante
		game.executeMove(Square.c7, Square.c5);

		// Podemos capturarlo
		legalMoves = game.getPossibleMoves();
		assertTrue(contieneMove(legalMoves, Square.b5, Square.c6));
		assertEquals(22, legalMoves.size());

		// Pero NO lo capturamos
		game.executeMove(Square.h2, Square.h3);


		game.executeMove(Square.h7, Square.h6);

		// Ahora no podemos capturar el pawn pasante !!!
		legalMoves = game.getPossibleMoves();
		assertFalse(contieneMove(legalMoves, Square.b5, Square.c6));
	}
	
	
	protected boolean contieneMove(MoveContainerReader movimientos, Square from, Square to) {
		for (Move move : movimientos) {
			if (from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())) {
				return true;
			}
		}
		return false;
	}
	
	
	private Game getGame(String string) {		
		GameBuilder builder = new GameBuilder(new ChessFactoryDebug());

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getResult();
	}
	
	
	private Game getDefaultGame() {				
		return getGame(FENDecoder.INITIAL_FEN);
	}		
	
}
