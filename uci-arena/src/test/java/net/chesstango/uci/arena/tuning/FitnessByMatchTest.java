package net.chesstango.uci.arena.tuning;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.pgn.PGNGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class FitnessByMatchTest {

    private FitnessByMatch fitnessFn;

    @BeforeEach
    public void setup() {
        fitnessFn = new FitnessByMatch();
    }


    @Test
    public void testCreateResult01() {

        Game game = FENDecoder.loadGame("8/P7/5Q1k/3p3p/3P2P1/1P1BP3/5P2/3K4 b - - 5 48");

        PGNGame pgnGame = PGNGame.createFromGame(game);

        int points = FitnessByMatch.getPoints(pgnGame);

        assertEquals(FitnessByMatch.WINNER_POINTS +
                6 * FitnessByMatch.getPieceValue(Piece.PAWN_WHITE) +
                2 * Math.abs(FitnessByMatch.getPieceValue(Piece.PAWN_BLACK)) +
                1 * FitnessByMatch.getPieceValue(Piece.QUEEN_WHITE) +
                1 * FitnessByMatch.getPieceValue(Piece.BISHOP_WHITE) +
                1 * FitnessByMatch.getPieceValue(Piece.KING_WHITE) +
                1 * Math.abs(FitnessByMatch.getPieceValue(Piece.KING_BLACK)
                ), points);
    }

    @Test
    public void testCreateResult02() {
        Game game = FENDecoder.loadGame("3k4/5p2/1p1bp3/3p2p1/3P3P/5q1K/p7/8 w - - 0 48");

        PGNGame pgnGame = PGNGame.createFromGame(game);

        int points = FitnessByMatch.getPoints(pgnGame);


        assertEquals(-1 * (FitnessByMatch.WINNER_POINTS +
                6 * FitnessByMatch.getPieceValue(Piece.PAWN_WHITE) +
                2 * Math.abs(FitnessByMatch.getPieceValue(Piece.PAWN_BLACK)) +
                1 * FitnessByMatch.getPieceValue(Piece.QUEEN_WHITE) +
                1 * FitnessByMatch.getPieceValue(Piece.BISHOP_WHITE) +
                1 * FitnessByMatch.getPieceValue(Piece.KING_WHITE) +
                1 * Math.abs(FitnessByMatch.getPieceValue(Piece.KING_BLACK))
        ), points);
    }


    @Test
    public void testCreateResultDraw01() {
        Game game = FENDecoder.loadGame("6Q1/P7/7k/3p3p/3P3P/1P1BP3/5P2/3K4 b - - 5 48");

        PGNGame pgnGame = PGNGame.createFromGame(game);

        int points = FitnessByMatch.getPoints(pgnGame);

        assertEquals(
                6 * FitnessByMatch.getPieceValue(Piece.PAWN_WHITE) +
                        2 * FitnessByMatch.getPieceValue(Piece.PAWN_BLACK) +
                        1 * FitnessByMatch.getPieceValue(Piece.QUEEN_WHITE) +
                        1 * FitnessByMatch.getPieceValue(Piece.BISHOP_WHITE) +
                        1 * FitnessByMatch.getPieceValue(Piece.KING_WHITE) +
                        1 * FitnessByMatch.getPieceValue(Piece.KING_BLACK)
                , points);
    }

    @Test
    public void testCreateResultDraw02() {
        Game game = FENDecoder.loadGame("3k4/5p2/1p1bp3/3p3p/3P3P/7K/p7/6q1 w - - 5 48");

        PGNGame pgnGame = PGNGame.createFromGame(game);

        int points = FitnessByMatch.getPoints(pgnGame);

        assertEquals(
                6 * FitnessByMatch.getPieceValue(Piece.PAWN_BLACK) +
                        2 * FitnessByMatch.getPieceValue(Piece.PAWN_WHITE) +
                        1 * FitnessByMatch.getPieceValue(Piece.QUEEN_BLACK) +
                        1 * FitnessByMatch.getPieceValue(Piece.BISHOP_BLACK) +
                        1 * FitnessByMatch.getPieceValue(Piece.KING_BLACK) +
                        1 * FitnessByMatch.getPieceValue(Piece.KING_WHITE)
                , points);
    }

    @Test
    public void testPieceValues() {
        assertTrue(FitnessByMatch.getPieceValue(Piece.PAWN_WHITE) - FitnessByMatch.getPieceValue(Piece.PAWN_BLACK) == 2 * FitnessByMatch.getPieceValue(Piece.PAWN_WHITE));
        assertTrue(FitnessByMatch.getPieceValue(Piece.ROOK_WHITE) - FitnessByMatch.getPieceValue(Piece.ROOK_BLACK) == 2 * FitnessByMatch.getPieceValue(Piece.ROOK_WHITE));
        assertTrue(FitnessByMatch.getPieceValue(Piece.KNIGHT_WHITE) - FitnessByMatch.getPieceValue(Piece.KNIGHT_BLACK) == 2 * FitnessByMatch.getPieceValue(Piece.KNIGHT_WHITE));
        assertTrue(FitnessByMatch.getPieceValue(Piece.BISHOP_WHITE) - FitnessByMatch.getPieceValue(Piece.BISHOP_BLACK) == 2 * FitnessByMatch.getPieceValue(Piece.BISHOP_WHITE));
        assertTrue(FitnessByMatch.getPieceValue(Piece.QUEEN_WHITE) - FitnessByMatch.getPieceValue(Piece.QUEEN_BLACK) == 2 * FitnessByMatch.getPieceValue(Piece.QUEEN_WHITE));
        assertTrue(FitnessByMatch.getPieceValue(Piece.KING_WHITE) - FitnessByMatch.getPieceValue(Piece.KING_BLACK) == 2 * FitnessByMatch.getPieceValue(Piece.KING_WHITE));
    }

}
