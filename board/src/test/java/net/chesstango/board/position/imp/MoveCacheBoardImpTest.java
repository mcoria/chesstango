package net.chesstango.board.position.imp;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.MoveCacheBoard;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoardImpTest {

	private MoveCacheBoard cache;

	private MoveFactory moveFactoryImp;

	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryWhite();
		cache = new MoveCacheBoardImp();
	}

	@Test
	public void test01() {
		MoveGeneratorResult result = new MoveGeneratorResult(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE));
		result.addPseudoMove(moveFactoryImp.createSimpleMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a3, null)));
		result.addPseudoMove(moveFactoryImp.createSimpleMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a4, null)));
		cache.setPseudoMoves(Square.a2, result);

		assertNotNull(cache.getPseudoMovesResult(Square.a2));
	}


	//Test
	public void possibleAffectedPseudoMovesPositions(){
		long posiciones[] = new long[64];
		for (Square square: Square.values()) {
			long possibleAffects = 0;
			for (Cardinal cardinal: Cardinal.values()){
				possibleAffects |= cardinal.getSquaresInDirection(square);
			}
			possibleAffects |= square.getKingJumps();
			possibleAffects |= square.getKnightJumps();
			posiciones[square.toIdx()] = possibleAffects;
		}

		for (long posicion : posiciones) {
			System.out.println(String.format("0x%sL,", Long.toHexString(posicion)));
		}
	}

	private Game getGame(String string) {
		GameBuilder builder = new GameBuilder(new ChessFactoryDebug() {
			@Override
			public MoveCacheBoardImp createMoveCacheBoard() {
				return new MoveCacheBoardForTest();
			}
		});

		FENDecoder parser = new FENDecoder(builder);

		parser.parseFEN(string);

		return builder.getChessRepresentation();
	}

	private class MoveCacheBoardForTest extends MoveCacheBoardDebug{

	}

}
