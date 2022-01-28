package chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.Game.GameStatus;
import chess.builder.ChessPositionBuilderGame;
import chess.debug.builder.DebugChessFactory;
import chess.fen.FENDecoder;
import chess.moves.imp.MoveFactoryBlack;


/**
 * @author Mauricio Coria
 *
 */
public class GameTest {

	
	@Test
	public void testPosicionInicial() {
		Game game =  getDefaultGame();
		
		assertEquals(Color.WHITE, game.getTurnoActual());
		assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());
		assertEquals(20, game.getPossibleMoves().size());
	}

	@Test
	public void testJuegoJaqueMate() {
		Game game =  getDefaultGame();
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getTurnoActual());
		
		game.executeMove(Square.e2, Square.e4);
		game.executeMove(Square.e7, Square.e5);
		game.executeMove(Square.f1, Square.c4);	
		game.executeMove(Square.b8, Square.c6);
		game.executeMove(Square.d1, Square.f3);
		game.executeMove(Square.f8, Square.c5);
		game.executeMove(Square.f3, Square.f7);
		
		assertEquals(Color.BLACK, game.getTurnoActual());
		assertEquals(GameStatus.JAQUE_MATE, game.getGameStatus());
		assertTrue(game.getPossibleMoves().isEmpty());
	}

	@Test
	public void testJuegoJaqueMateUndo() {
		Game game =  getDefaultGame();
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getTurnoActual());
		
		game.executeMove(Square.e2, Square.e4);
		game.executeMove(Square.e7, Square.e5);
		game.executeMove(Square.f1, Square.c4);	
		game.executeMove(Square.b8, Square.c6);
		game.executeMove(Square.d1, Square.f3);
		game.executeMove(Square.f8, Square.c5);
		game.executeMove(Square.f3, Square.f7);
		
		assertEquals(Color.BLACK, game.getTurnoActual());
		assertEquals(GameStatus.JAQUE_MATE, game.getGameStatus());
		assertTrue(game.getPossibleMoves().isEmpty());
		
		game.undoMove();
		game.undoMove();
		game.undoMove();	
		game.undoMove();
		game.undoMove();
		game.undoMove();
		game.undoMove();
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getTurnoActual());		
	}	
	
	@Test
	public void testJuegoJaque() {
		Game game =  getDefaultGame();
		
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getTurnoActual());
		
		game.executeMove(Square.e2, Square.e4);
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.BLACK, game.getTurnoActual());		
		
		game.executeMove(Square.e7, Square.e5);
		assertEquals(29, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getTurnoActual());			
		
		game.executeMove(Square.f1, Square.c4);
		assertEquals(29, game.getPossibleMoves().size());
		assertEquals(Color.BLACK, game.getTurnoActual());			
		
		game.executeMove(Square.b8, Square.c6);
		assertEquals(33, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getTurnoActual());			
		
		game.executeMove(Square.d1, Square.f3);
		assertEquals(31, game.getPossibleMoves().size());
		assertEquals(Color.BLACK, game.getTurnoActual());			
		
		game.executeMove(Square.g8, Square.h6);
		assertEquals(42, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getTurnoActual());			
		
		
		game.executeMove(Square.f3, Square.f7);
		assertEquals(Color.BLACK, game.getTurnoActual());
		assertEquals(GameStatus.JAQUE, game.getGameStatus());
		assertEquals(1, game.getPossibleMoves().size());
	}
	
	@Test
	public void testJuegoTablas() {
		Game game =  getGame("k7/7Q/K7/8/8/8/8/8 w - - 0 1");
		
		assertEquals(Color.WHITE, game.getTurnoActual());
		
		game.executeMove(Square.h7, Square.c7);

		assertEquals(Color.BLACK, game.getTurnoActual());
		assertEquals(GameStatus.TABLAS, game.getGameStatus());
		assertEquals(0, game.getPossibleMoves().size());
	}
	
	@Test
	public void testJuegoUndo() {
		Game game =  getDefaultGame();
		
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getTurnoActual());
		
		game.executeMove(Square.e2, Square.e4);
		
		game.undoMove();
		assertEquals(20, game.getPossibleMoves().size());
		assertEquals(Color.WHITE, game.getTurnoActual());
	}
	
	@Test
	public void testJuegoNoPawnPasante() {
		Game game =  getGame("rnbqkbnr/p1pppppp/1p6/P7/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 2");
		
		game.executeMove(Square.b6, Square.b5);
		
		assertEquals(22, game.getPossibleMoves().size());
	}
	
	@Test
	public void testJuegoPawnPasante() {
		Game game =  getGame("rnbqkbnr/1ppppppp/8/pP6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2");
		
		game.executeMove(Square.c7, Square.c5);
		
		assertNotNull(game.getMovimiento(Square.b5, Square.c6));
		assertEquals(22, game.getPossibleMoves().size());
	}
	
	
	//TODO: No solo cuando se mueve la torre sino tambien cuando la capturan
	@Test 
	public void testMueveRookPierdeEnroque() {
		Game game =  getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
		
		game.executeMove(Square.a1, Square.b1);
		
		assertFalse(game.getTablero().getBoardState().isCastlingWhiteQueenAllowed());
		
		assertEquals(43, game.getPossibleMoves().size());
	}	

	@Test
	public void testJuegoKiwipeteTestUndo() {
		Game game =  getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
		
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
		Game game =  getGame("4k2r/8/8/8/3B4/8/8/4K3 w k -");
		
		//Estado inicial
		assertEquals(18, game.getPossibleMoves().size());
		
		//Movimiento 1 - cualquier movimiento
		game.executeMove(Square.d4, Square.c3);
		assertEquals(15, game.getPossibleMoves().size()); 
		//CastlingBlackKingMove es uno de los movimientos posibles
		assertEquals(MoveFactoryBlack.castlingKingMove, game.getMovimiento(Square.e8, Square.g8));
		
		//Undo movimiento 1 y volvemos al estado inicial
		game.undoMove();
		
		//Estado inicial
		assertEquals(18, game.getPossibleMoves().size());
		
		//Capturamos la torre negra
		game.executeMove(Square.d4, Square.h8);
		assertEquals(5, game.getPossibleMoves().size());
		//Ya no tenemos enroque
		assertNull(game.getMovimiento(Square.e8, Square.h8));
		
		//Undo captura de torre negra ---- Volvemos al estado inicial
		game.undoMove();  // Aca esta el problema, el UNDO no borra los movimientos de king del cache
		assertEquals(18, game.getPossibleMoves().size());
		
		//Movimiento 1 - lo repetimos
		game.executeMove(Square.d4, Square.c3);
		assertEquals(15, game.getPossibleMoves().size()); 
		//CastlingBlackKingMove es uno de los movimientos posibles
		assertEquals(MoveFactoryBlack.castlingKingMove, game.getMovimiento(Square.e8, Square.g8));
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
	
	private Game getGame(String string) {		
		ChessPositionBuilderGame builder = new ChessPositionBuilderGame(new DebugChessFactory());
		//ChessBuilderGame builder = new ChessBuilderGame();

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getResult();
	}
	
	
	private Game getDefaultGame() {				
		return getGame(FENDecoder.INITIAL_FEN);
	}		
	
}
