package net.chesstango.search.sorters.groupsorters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class CatchAllGroupSorterTest {

    private CatchAllGroupSorter catchAllGroupSorter;

    @BeforeEach
    public void setUp() {
        catchAllGroupSorter = new CatchAllGroupSorter();
    }

    @Test
    public void testEmptyRange() {
        Move move1 = createMove(PiecePositioned.of(Square.e2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.e4));
        Move move2 = createMove(PiecePositioned.of(Square.d2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.d4));

        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = catchAllGroupSorter.sort(moves, 1, 1);

        assertEquals(1, result);
        assertEquals(move1, moves.get(0));
        assertEquals(move2, moves.get(1));
    }

    @Test
    public void testSortFullList() {
        Move moveQueen = createMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveKnight = createMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveBishop = createMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveRook = createMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE), PiecePositioned.getPosition(Square.e3));
        Move movePawn = createMove(PiecePositioned.of(Square.e2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveKing = createMove(PiecePositioned.of(Square.e2, Piece.KING_WHITE), PiecePositioned.getPosition(Square.e3));

        List<Move> moves = new ArrayList<>(List.of(moveKnight, moveKing, moveBishop, moveQueen, moveRook, movePawn));

        int result = catchAllGroupSorter.sort(moves, 0, moves.size());

        assertEquals(moves.size(), result);
        assertEquals(List.of(moveQueen, moveKnight, moveBishop, moveRook, movePawn, moveKing), moves);
    }

    @Test
    public void testSortSubRange() {
        Move head = createMove(PiecePositioned.of(Square.a1, Piece.ROOK_WHITE), PiecePositioned.getPosition(Square.a2));
        Move moveQueen = createMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveKnight = createMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveBishop = createMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveRook = createMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE), PiecePositioned.getPosition(Square.e3));
        Move tail = createMove(PiecePositioned.of(Square.h1, Piece.ROOK_WHITE), PiecePositioned.getPosition(Square.h2));

        List<Move> moves = new ArrayList<>(List.of(head, moveKnight, moveBishop, moveQueen, moveRook, tail));

        int result = catchAllGroupSorter.sort(moves, 1, 5);

        assertEquals(5, result);
        assertEquals(head, moves.get(0));
        assertEquals(moveQueen, moves.get(1));
        assertEquals(moveKnight, moves.get(2));
        assertEquals(moveBishop, moves.get(3));
        assertEquals(moveRook, moves.get(4));
        assertEquals(tail, moves.get(5));
    }

    @Test
    public void testSortGameMoves() {
        Game game = Game.from(FEN.from("rnb1kbnr/pppp1ppp/4p3/5q2/4P2N/8/PPPP1PPP/RNBQKB1R w KQkq - 4 4"));

        MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
        List<Move> moves1 = new ArrayList<>(possibleMoves.size());
        List<Move> moves2 = new ArrayList<>(possibleMoves.size());

        for (Move move : possibleMoves) {
            moves1.add(move);
            moves2.add(move);
        }

        int result = catchAllGroupSorter.sort(moves1, 0, moves1.size());

        assertEquals(moves1.size(), result);

        moves2.sort(new DefaultMoveComparator().reversed());

        assertEquals(moves2, moves1);
    }

    private Move createMove(PiecePositioned from, PiecePositioned to) {
        return new Move() {
            @Override
            public PiecePositioned getFrom() {
                return from;
            }

            @Override
            public PiecePositioned getTo() {
                return to;
            }

            @Override
            public void executeMove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void undoMove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Cardinal getMoveDirection() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isQuiet() {
                throw new UnsupportedOperationException();
            }

            @Override
            public long getZobristHash() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
