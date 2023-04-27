package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;



/**
 * @author Mauricio Coria
 */
public abstract class MateIn1Test {


    public abstract SearchMove getBestMoveFinder();

    @Test
    public void testQueenWhiteCheckMate() {
        // Jaque Mate en movimiento de QUEEN_WHITE
        Game game = FENDecoder.loadGame("rnbqkbnr/2pppppp/8/pp4N1/8/4PQ2/PPPP1PPP/RNB1KB1R w KQkq - 0 5");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 1);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.f3, smartMove.getFrom().getSquare());
        assertEquals(Square.f7, smartMove.getTo().getSquare());

        assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }

    @Test
    public void testQueenBlackCheckMate() {
        // Jaque Mate en movimiento de QUEEN_BLACK
        Game game = FENDecoder.loadGame("rnb1kb1r/pppp1ppp/4pq2/PN6/1P4n1/8/2PPPPPP/R1BQKBNR b KQkq - 0 7");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 1);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.f6, smartMove.getFrom().getSquare());
        assertEquals(Square.f2, smartMove.getTo().getSquare());

        assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());
    }

    @Test
    public void testKingTrapped() {
        // Jaque Mate en movimiento de QUEEN_WHITE	(rey esta solo y atrapado por torre blanca)
        Game game = FENDecoder.loadGame("1k6/6R1/7Q/8/2KP3P/5P2/4P1P1/1N3BNR w - - 0 40");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 1);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.h6, smartMove.getFrom().getSquare());
        assertEquals(Square.h8, smartMove.getTo().getSquare());

        assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }


    @Test
    public void testFoolsMateTest() {
        // Fool's mate
        Game game = FENDecoder.loadGame("rnbqkbnr/pppp1ppp/4p3/8/6P1/5P2/PPPPP2P/RNBQKBNR b KQkq - 0 2");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 1);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.d8, smartMove.getFrom().getSquare());
        assertEquals(Square.h4, smartMove.getTo().getSquare());

        assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());
    }

}
